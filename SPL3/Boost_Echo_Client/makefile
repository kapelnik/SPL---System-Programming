CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system

all: STOMPClient
	g++ -pthread -o bin/STOMPClient bin/Books.o bin/ConnectionHandler.o bin/Keyboard.o  bin/STOMPClient.o $(LDFLAGS)

STOMPClient: bin/Books.o bin/ConnectionHandler.o bin/Keyboard.o bin/STOMPClient.o
	@echo 'Building target: STOMPClient'
	@echo 'Invoking: C++ Linker'
	@echo 'Finished building target: STOMPClient'
	@echo ' '

bin/Books.o: src/STOMP/Books.cpp
	g++ -pthread $(CFLAGS) -o bin/Books.o src/STOMP/Books.cpp

bin/ConnectionHandler.o: src/STOMP/ConnectionHandler.cpp
	g++ -pthread $(CFLAGS) -o bin/ConnectionHandler.o src/STOMP/ConnectionHandler.cpp

bin/Keyboard.o: src/STOMP/Keyboard.cpp
	g++ -pthread $(CFLAGS) -o bin/Keyboard.o src/STOMP/Keyboard.cpp


bin/STOMPClient.o: src/STOMPClient.cpp
	g++ -pthread $(CFLAGS) -o bin/STOMPClient.o src/STOMPClient.cpp


.PHONY: clean
clean:
	rm -f bin/*