//
// Created by kapelnik@wincs.cs.bgu.ac.il on 11/01/2020.
//

#include "../../include/Books.h"
#include <vector>
#include <iostream>

using namespace std;
//book [name,owner,isavailable]

Books::Books(){}
void Books::addBook(Book* book){
    books.push_back(book);
}
vector<Book*> Books::getAllBooks(){
    return books;
}
void Books::removeBook(string name){
    int i=0;
    for(Book* b:books){
        if (b->getName()==name)
            books.erase(books.begin()+i);
        i+=1;
    }
}
Book* Books::getBook(string name){
    for(Book* b:books){
        if (b->getName()==name)
            return b;
    }
    return new Book("","","", false);
}
Books* Books::getBooksByGenre(string genre){
    Books* booksByGenre = new Books();
    for(Book* b:books){
        if (b->getGenre()==genre)
            booksByGenre->addBook(b);
    }
    return booksByGenre;
}

string &Books::getMyname(){
    return myname;
}

void Books::addAskedBook(Book* book){
    booksiAskedFor.push_back(book);
}
void Books::removeAskedBook(Book* book){
    int i=0;
    for(auto x:booksiAskedFor)
        if(x->getName()==book->getName()){
            booksiAskedFor.erase(booksiAskedFor.begin()+i);
            i+=1;
        }
}

vector<Book*> &Books::getBooksiAskedFor(){
    return booksiAskedFor;
}

void Books::setMyname(string &myname) {
    this->myname = myname;
}
//-------------BOOK----------------
//-------------BOOK----------------
//-------------BOOK----------------
const string &Book::getName() const {
    return name;
}

void Book::setName(const string &name) {
    Book::name = name;
}
bool Book::isAvailable1() const {
    return isAvailable;
}

void Book::setIsAvailable(bool isAvailable) {
    Book::isAvailable = isAvailable;
}

const string &Book::getLender() const {
    return lender;
}

void Book::setLender(const string &lender) {
    Book::lender = lender;
}

const string &Book::getGenre() const {
    return genre;
}

void Book::setGenre(const string &genre) {
    Book::genre = genre;
}

bool Book::isAvailable2() const {
    return isAvailable;
}

Book::Book(string name, string lender, string genre, bool isAvailable):name{name},lender{lender},genre{genre},isAvailable{isAvailable} {

}

bool Book::isAvailable3() const {
    return isAvailable;
}
