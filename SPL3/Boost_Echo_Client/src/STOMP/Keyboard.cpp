//
// Created by amit on 1/14/20.
//

#include "../include/Keyboard.h"
#include "../include/ConnectionHandler.h"
#include "../include/Books.h"
#include <../include/STOMPClient.h>


#include <unordered_map>
#include <iostream>
#include <cassert>
#include <mutex>

using namespace std;
class Keyboard {
private:
    ConnectionHandler* handler;
    std::mutex& mutex;
    Books *mybooks;
    bool terminate;
    int disconnectFlag;

public:
        Keyboard (ConnectionHandler* handler, Books *mybooks, std::mutex& mutex,bool& terminate) : handler(handler), mybooks(mybooks), mutex(mutex),terminate(terminate) {
            disconnectFlag=-1;
        }

//eyboard thread
        void process() {
        const short bufsize = 1024;
        int id = 0;
        int receiptnumber = 0;
        string myname;
        unordered_map<string, int> genreIdMap;
        string stompframe;
        char buf[bufsize];

            while (!terminate) {
                stompframe = "";
                if (disconnectFlag == -1) // only if not waiting for receipt for the Disconnect
                    cin.getline(buf, bufsize); // blocked
                string line(buf);

                int len = line.length();

                //MAKE STOMP FROM LINE

                int spaceindex = line.find_first_of(' ');
                string firstword = line.substr(0, spaceindex); //command
                line = line.substr(spaceindex + 1);
                //now checking for command:
                if (firstword == "login") {
                    spaceindex = line.find_first_of(' ');
                    string hostport = line.substr(0, spaceindex);//host:port
                    line = line.substr(spaceindex + 1);

                    spaceindex = line.find_first_of(' ');
                    string loginName = line.substr(0, spaceindex);//login name
                    line = line.substr(spaceindex + 1);

                    spaceindex = line.find_first_of(' ');
                    string passcode = line.substr(0, spaceindex); //passcode
                    line = line.substr(spaceindex + 1);

                    myname = loginName;
                    mybooks->setMyname(myname);
                    stompframe = "CONNECT\n"
                                 "accept-version:1.2\n"
                                 "host:" + hostport +
                                 "\nlogin:" + loginName +
                                 "\npasscode:" +
                                 passcode +
                                 "\n\n\0";
                    int j = hostport.find_first_not_of(':');
                    string host = hostport.substr(0,j-1);
                    int port = stoi(hostport.substr(j,hostport.length()-j));
                    ConnectionHandler handlerNew(host,port);
                    handler = &handlerNew;
                    STOMPClient::setDisconnectFlag(false);
                    if (!handler->connect()) {
                        std::cerr << "Cannot connect to " << hostport  << std::endl;
                                            }
                    std::cout<<"connected to server"<<std::endl;

                }
                if (firstword == "join") {
                    string genre = line.substr(0, line.find_first_of(' '));
                    stompframe = "SUBSCRIBE"
                                 "\ndestination:" + genre +
                                 "\nid:" + to_string(id) +
                                 "\nreceipt:" + to_string(receiptnumber) +
                                 "\n\n\0";
                    id++;
                    receiptnumber++;
                    genreIdMap[genre] = id;
                }
                if (firstword == "exit") {
                    string genre = line;

                    stompframe = "UNSUBSCRIBE\nreceipt:" + to_string(genreIdMap[genre]) + "\n\n\0";
                    genreIdMap.erase(genre); // deletes it from the map
                }
                if (firstword == "add") {
                    spaceindex = line.find_first_of(' ');
                    string genre = line.substr(0, spaceindex); ///genre
                    line = line.substr(spaceindex + 1);       ///line=book name

                    Book *book = new Book(line, mybooks->getMyname(), genre, true);
                    mybooks->addBook(book);

                }


                if (firstword == "borrow") {
                    spaceindex = line.find_first_of(' ');
                    string genre = line.substr(0, spaceindex);
                    line = line.substr(spaceindex + 1);       ///line=book name
                    stompframe = "SEND\ndestination:" + genre +
                                 "\n\n" + myname + " wish to borrow " + line + "\n\0";
                    mybooks->addAskedBook(new Book(line, "unknown", genre, false));

                }
                if (firstword == "return") {
                    spaceindex = line.find_first_of(' ');
                    string genre = line.substr(0, spaceindex);
                    line = line.substr(spaceindex + 1);       ///line=book name

                    stompframe = "SEND\ndestination:" + genre +
                                 "\n\nReturning " + line + " to " + mybooks->getBook(line)->getLender() + "\n\0";

                    mybooks->removeBook(line);
                }
                if (firstword == "logout") {
                    stompframe = "DISCONNECT"
                                 "\nreceipt:" + to_string(receiptnumber) + "\n\n\0";
                    STOMPClient::setDisconnectFlag(receiptnumber);
                    disconnectFlag = receiptnumber;
                    receiptnumber++;

                }


                if (firstword == "books")
                    for (auto x :mybooks->getAllBooks()) {
                        if (x->isAvailable1())
                            cout << "book name:" + x->getName() + " available. lender:" + x->getLender() << endl;
                        else
                            cout << "book name:" + x->getName() + " not Available. lender:" +
                                    x->getLender() << endl;
                    }

                //ALREADY AS STOMP, SEND AS BYTES TO SERVER:
                if (stompframe.size() > 0) {
                    cout << "Client:\n" + stompframe + "@" << endl;

                    mutex.lock();
                    if (!handler->sendLine(stompframe)) {
                        std::cout << "Disconnected. Exiting...\n" << std::endl;
                        terminate = true;
                    }
                    mutex.unlock();

                }


            }
        }

};