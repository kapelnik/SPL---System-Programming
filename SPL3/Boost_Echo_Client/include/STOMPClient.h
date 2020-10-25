//
// Created by amit on 1/15/20.
//

#ifndef BOOST_ECHO_CLIENT_STOMPCLIENT_H
#define BOOST_ECHO_CLIENT_STOMPCLIENT_H

#include <iostream>

class STOMPClient {
public:
    int main(int argc, char *argv[]);
    static std::string getUntilDelimiter(char del);
    static void setDisconnectFlag(int disconnectFlag);

};


#endif //BOOST_ECHO_CLIENT_STOMPCLIENT_H
