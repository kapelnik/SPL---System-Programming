//
// Created by amit on 1/15/20.
//

#include "STOMPClient.h"
#include <stdlib.h>
#include <ConnectionHandler.h>
#include <Books.h>
#include "Keyboard.cpp"

#include <thread>
#include <iostream>
#include <mutex>
/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
using namespace std;
Books* mybooks;
string answer;
int disconnectFlag=-1;
int main() {
    bool closeAll=false;

    while (!closeAll) {
        bool terminate= true;

        std::string host = "";
        short port = 0;
        ConnectionHandler handler(host, port);
        ConnectionHandler* handler_ptr = &handler;

        mybooks= new Books();

        std::mutex mutex;
        Keyboard task1(handler_ptr,mybooks, mutex , terminate);
        std::thread th1(&Keyboard::process, &task1);
        string stompframe;

        while (!terminate) {
            stompframe = "";
            answer = "";
            if (!handler.getLine(answer)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }

            cout << "Server:\n" + answer << endl;//TODO delete..
            string command = STOMPClient::getUntilDelimiter('\n');
            if (command == "RECEIPT") {
                STOMPClient::getUntilDelimiter(':'); // to the trash, its the header name
                string receiptId = STOMPClient::getUntilDelimiter('\n');
                if (to_string(disconnectFlag) == receiptId)
                    terminate = true;// TODO: close keyboard and check the receipt id
            } else if (command == "MESSAGE") {
                STOMPClient::getUntilDelimiter(':'); // to the trash, its the header name
                string genre = STOMPClient::getUntilDelimiter('\n');
                STOMPClient::getUntilDelimiter('\n');
                string body = answer;
                if (body == "book status") {
                    Books *toSend = mybooks->getBooksByGenre(genre);
                    if (!toSend->getAllBooks().empty()) {
                        stompframe = "SEND"
                                     "\ndestination:" + genre +
                                     "\n"
                                     + mybooks->getMyname() + ":";
                        for (Book *b:toSend->getAllBooks()) {
                            stompframe += b->getName() + ",";
                        }
                        stompframe.substr(0, stompframe.length() - 1); //removes the last comma TODO: check the sizes
                        stompframe += "\n\0";
                    }

                } else { // no book status
                    string user = STOMPClient::getUntilDelimiter(' ');
                    if (answer.substr(0, 4) == "wish")//  "wish to borrow {book name}")
                    {
                        STOMPClient::getUntilDelimiter(' ');// wish
                        STOMPClient::getUntilDelimiter(' ');// to
                        STOMPClient::getUntilDelimiter(' ');// borrow
                        string bookName = answer;
                        //if i have it SEND {username} has {book name}
                        for (Book *b:mybooks->getBooksByGenre(genre)->getAllBooks()) {
                            if ((b->getName() == bookName) && (b->isAvailable1())) {
                                stompframe = "SEND"
                                             "\ndestination:" + genre +
                                             "\n"
                                             + mybooks->getMyname() + " has " + bookName;
                            }
                        }

                    } else if (user == "Returning") {// “Returning {book name} to {book lender}”
                        string bookName = STOMPClient::getUntilDelimiter(' '); // {book name}
                        STOMPClient::getUntilDelimiter(' ');// to
                        string lender = STOMPClient::getUntilDelimiter(' '); // {book lender}
                        if (lender == mybooks->getMyname()) {
                            mybooks->getBook(bookName)->setIsAvailable(true);
                        }

                    } else if (answer.substr(0, 3) == "has")// "has {book name}-some else has it
                    {
                        STOMPClient::getUntilDelimiter(' ');// has
                        string bookName = answer; // {book name}
                        for (Book *b:mybooks->getBooksiAskedFor()) {
                            if (b->getName() == bookName) {
                                //SEND Taking {book name} from {book owner username}
                                stompframe = "SEND"
                                             "\ndestination:" + genre +
                                             "\n\nTaking " + bookName + " from " + user;
                                mybooks->addBook(new Book(bookName, user, genre, true));
                                mybooks->removeAskedBook(b);
                                break;
                            }
                        }
                    } else { //Taking {book name} from {book owner username}
                        string bookName = STOMPClient::getUntilDelimiter(' ');//  {book name}
                        STOMPClient::getUntilDelimiter(' ');// from
                        string owner = STOMPClient::getUntilDelimiter(' ');//  {book owner}
                        if (mybooks->getMyname() == owner) {
                            mybooks->getBook(bookName)->setIsAvailable(false);
                        }
                    }
                }


            } else if (command == "CONNECTED") {
                cout << "Login successful" << endl;
            }

            if (stompframe != "") //sending the frame to the Server
            {
                mutex.lock();
                handler.sendLine(stompframe);
                mutex.unlock();
            }
        }
    }

    return 0;
}


string STOMPClient::getUntilDelimiter(char del) {
    int i = answer.find_first_of(del);
    string toReturn = answer.substr(0,i);
    answer = answer.substr(i+1);
    return toReturn;
}

void STOMPClient::setDisconnectFlag(int receiptId) {
    disconnectFlag = receiptId;
}


