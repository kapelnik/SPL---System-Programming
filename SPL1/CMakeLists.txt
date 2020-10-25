cmake_minimum_required(VERSION 3.15)
project(spl1)

set(CMAKE_CXX_STANDARD 11)
include_directories({include})

add_executable(spl1
        src/Main.cpp
        src/Action.cpp
        src/Session.cpp
        src/User.cpp
        src/Watchable.cpp
        include/Action.h
        include/json.hpp
        include/Session.h
        include/User.h
        include/Watchable.h
        )

