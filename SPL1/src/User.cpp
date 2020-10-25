//
// Created by kapelnik on 23/11/2019.
//

#include "../include/User.h"
#include "../include/Watchable.h"

#include "../include/Session.h"
#include<bits/stdc++.h>

//#include <nlohmann/json.hpp>
using namespace std;
    User::User(const std::string &name):history{},name{name}{}

    std::string User::getName() const {
        return this->name;
    }
    std::vector<Watchable*>& User::get_history() {
        return history;
    }
    bool User::searchinhistory(int id) {
        for (auto watch : history) {
            if (watch->get_id() == id)
                return true;
        }
            return false;

    }


//class LengthRecommenderUser : public User {
LengthRecommenderUser::LengthRecommenderUser(const std::string &name):User(name){}//use USER constractor}

    Watchable* LengthRecommenderUser::getRecommendation(Session& sess) {
        int avg_his_length=0;
        for(auto x : history)
            avg_his_length+=x->get_length();
        avg_his_length = (int)(avg_his_length/history.size());
        int min=99999, id=0;
        for(auto x : sess.get_content()){
           if(!searchinhistory((int)x->get_id()))
                if(abs(x->get_length()-avg_his_length)<min) {
                    id = (int)x->get_id();
                    min = abs(x->get_length()-avg_his_length);
                }
        }
        return sess.get_content().at(id);

}

User* LengthRecommenderUser::clone(const std::string newName) {
    return  new LengthRecommenderUser(*this);
/*
    LengthRecommenderUser *user = new LengthRecommenderUser(newName);
    //user->history=this->history;
    user->avg_his_length=this->avg_his_length;
    return user;
*/
 }


//class RerunRecommenderUser : public User {
RerunRecommenderUser::RerunRecommenderUser(const std::string& name):User{name},towatch{0}{}//use USER constractor
    Watchable* RerunRecommenderUser::getRecommendation(Session& s) {
        if(((int)(history.size()) == 1)|((int)(history.size()) == 2)|(towatch==((int)history.size()-1)))
            towatch=-1;
        towatch++;
        return history.at(towatch);
    }
User *RerunRecommenderUser::clone(std::string newName) {
    return new RerunRecommenderUser(*this);
    /*
    RerunRecommenderUser *user = new RerunRecommenderUser(newName);
    //user->history=this->history;
    // add more staff to copy uniq to return
    return user;*/
}



//class GenreRecommenderUser : public User {(history.size() == 1)
    GenreRecommenderUser::GenreRecommenderUser(const std::string& name):User(name){};//use USER constractor
    Watchable* GenreRecommenderUser::getRecommendation(Session& s){
        std::vector< std::pair<std::string,int>> favorite_tags;
        bool found = false;
        for(auto x: history){
            for( auto tag : x->get_tags()){
                for(auto &favtag : favorite_tags){
                    if(tag==favtag.first) {
                        favtag.second++;
                        found = true;
                    }                                                   ///arrange the tags vector ant sort it
                }
                if (!found) {
                    pair<std::string,int> newtag(tag,1);
                    favorite_tags.push_back(newtag);
                    found = false;
                }
                found = false;
            }

        }
        sort(favorite_tags.begin(), favorite_tags.end(), sortbysec);


        for(auto x : favorite_tags) {          //find best match for the user from the most popular tag to the least one
            for (auto y : s.get_content()) {
                if(!searchinhistory((int)(y->get_id()))) {
                    for (auto z : y->get_tags()) {
                        if (z==x.first) {
                            x.second+=1;
                            return y;       //bingo
                        }
                    }
                }
            }
        }
        //if we are here, the user already saw it all.
        cout<<"no more stuff to see for you"<< endl;
        return nullptr;
    }
User *GenreRecommenderUser::clone(std::string newName) {
   // GenreRecommenderUser *user = new GenreRecommenderUser(newName);
    //user->history=this->history;
    // add more staff to copy uniq to genre
    //return user;

    return new GenreRecommenderUser(*this);
}

bool GenreRecommenderUser::sortbysec(const std::pair<std::string, int> &a, const std::pair<std::string, int> &b) {
    return (a.second < b.second);
}
