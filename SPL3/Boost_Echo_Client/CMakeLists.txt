cmake_minimum_required(VERSION 3.15)
project(Boost_Echo_Client)

set(CMAKE_CXX_STANDARD 11)

include_directories(include)
link_libraries("-lboost_system -pthread")
add_executable(STOMPClient.cpp
        include/Books.h
        include/ConnectionHandler.h
        include/Keyboard.h
        include/STOMPClient.h
        src/STOMP/Books.cpp
        src/STOMP/STOMPClient.cpp
        src/STOMP/Keyboard.cpp
        src/STOMP/ConnectionHandler.cpp
        )
