//
// Created by amit on 11/25/19.
//
#include <string>
#include <iostream>
#include <sstream>
#include "../include/Action.h"
#include "../include/Session.h"
#include "../include/User.h"
#include "../include/Watchable.h"



using namespace std;

//enum ActionStatus{PENDING, COMPLETED, ERROR}

//class BaseAction::{
    BaseAction::BaseAction():errorMsg{},status{PENDING}{
}

void BaseAction::complete() {
status=COMPLETED;
}

void BaseAction::error(const std::string &errorMsg) {
    this->errorMsg=errorMsg;
    cout<<errorMsg<<endl;
    status=ERROR;
}

ActionStatus BaseAction::getStatus() const {
    return status;
}

std::string BaseAction::getStatusString(ActionStatus status) const {
    if (status == 0)
        return "PENDING";
    if (status==1)
        return "COMPLETED";
    return "ERROR";
    }

std::string BaseAction::get_errorMsg() const {
    return errorMsg;
}

void BaseAction::set_status(ActionStatus as) {
    this->status = as;
}

void BaseAction::set_errorMsg(std::string msg) {
    this->errorMsg= msg;
}

//class CreateUser  : public BaseAction {
    void CreateUser::act(Session& sess)
    {
        sess.add_actionlog(*this);
        std::vector<std::string> command = sess.get_command();
        if ((int)command.size()<3)
            error("The command is too short for Create User action");
        else if (command.at(2).compare("len")*command.at(2).compare("rer")*command.at(2).compare("gen")!=0) {
            error("Wrong recommendation algorithm, use only 'len','gen' or 'rer' ");
        }
        else if (sess.isTaken(command.at(1))){
            error("Name already exists");
        }
        else
        {
            User* newUser;
            if (command.at(2)=="len")
                newUser = new LengthRecommenderUser(command.at(1));
            else if (command.at(2)=="rer")
                newUser = new RerunRecommenderUser(command.at(1));
            else if (command.at(2)=="gen")
                newUser = new GenreRecommenderUser(command.at(1));
            sess.add_user(newUser);
            complete();
        }

    }
    std::string CreateUser::toString() const
    {
        string s = "CreateUser "  + getStatusString(getStatus());
        if (get_errorMsg()!="")
            s+=":" + get_errorMsg();
        return s;
    }

BaseAction *CreateUser::clone() {
    CreateUser* newAction = new CreateUser();
    newAction->set_status(this->getStatus());
    newAction->set_errorMsg(this->get_errorMsg());
    return newAction ;
}

//class ChangeActiveUser : public BaseAction {
    void ChangeActiveUser::act(Session& sess){
        sess.add_actionlog(*this);
        std::vector<std::string> command = sess.get_command();
        if ((int)command.size()<2)
            error("The command is too short for the action");
        else if (!sess.isTaken(command.at(1))) {
            error("This user name is not in the system");
        }
        else
        {
            sess.set_activeUser(command.at(1));
            complete();
        }
    }
    std::string ChangeActiveUser::toString() const {
        string s = "ChangeUser "  + getStatusString(getStatus());
        if (get_errorMsg()!="")
            s+=":" + get_errorMsg();
        return s;
    }

BaseAction *ChangeActiveUser::clone() {
    ChangeActiveUser* newAction = new ChangeActiveUser();
    newAction->set_status(this->getStatus());
    newAction->set_errorMsg(this->get_errorMsg());
    return newAction ;
}

//class DeleteUser : public BaseAction {
    void DeleteUser::act(Session & sess){
    sess.add_actionlog(*this);
    std::vector<std::string> command = sess.get_command();
    if ((int)command.size()<2)
        error("The command is too short for the action");
    else if (!sess.isTaken(command.at(1))) {
        error("This user name is not in the system");
    }
    else if (command.at(1)==sess.get_activeUser().getName()) {
        error("You can't delete the active user, change user first");
    }
    else
    {
        sess.delete_user(command.at(1));
        complete();
    }
}

    std::string DeleteUser::toString() const {
        std::string s = "deleteuser "  + getStatusString(getStatus());
        if (get_errorMsg()!="")
            s+=":" + get_errorMsg();
        return s;
    }

BaseAction *DeleteUser::clone() {
    DeleteUser* newAction = new DeleteUser();
    newAction->set_status(this->getStatus());
    newAction->set_errorMsg(this->get_errorMsg());
    return newAction ;
}


//class DuplicateUser : public BaseAction {
    void DuplicateUser::act(Session & sess){
    sess.add_actionlog(*this);
    std::vector<std::string> command = sess.get_command();
    if ((int)command.size()<3)
        error("The command is too short for the action");
    else if (!sess.isTaken(command.at(1))) {
        error("This user is not in the system");}
    else if (sess.isTaken(command.at(2))) {
        error("This user name is taken already");
    }
    else
    {
        User* newUser = sess.get_user_by_name(command.at(1))->clone(command.at(2));
        sess.add_user(newUser);
        complete();
    }
    } //make DuplicateUser var here in "act", to pull Name1 name2 from the start method
    std::string DuplicateUser::toString() const{
        string s = "DuplicateUser "  + getStatusString(getStatus());
        if (get_errorMsg()!="")
            s+=":" + get_errorMsg();
        return s;
    }

BaseAction *DuplicateUser::clone() {
    DuplicateUser* newAction = new DuplicateUser();
    newAction->set_status(this->getStatus());
    newAction->set_errorMsg(this->get_errorMsg());
    return newAction ;
}


//class PrintContentList : public BaseAction {
    void PrintContentList::act (Session& sess){
    sess.add_actionlog(*this);
    std::vector<Watchable*>& content =  sess.get_content();
    int id=1;
    for(auto v: content)
    {
        std::string s;
        s = "\n" + to_string(id++) +". "+ v->toString()+ " " + to_string(v->get_length()) +" minutes" +" [";

        for ( auto i : v->get_tags() ) // runs on all the vector in tags
            s +=  i + ", ";
        s = s.substr(0, (int)(s.size())-2); // remove the last ", "
        s+="]";
        cout<<s;
    }
    std::cout<<" \n";
}
    std::string PrintContentList::toString() const{
        string s = "PrintContentList "  + getStatusString(getStatus());
        if (get_errorMsg()!="")
            s+=":" + get_errorMsg();
        return s;
    }

BaseAction *PrintContentList::clone() {
    PrintContentList* newAction = new PrintContentList();
    newAction->set_status(this->getStatus());
    newAction->set_errorMsg(this->get_errorMsg());
    return newAction ;
}

//class PrintWatchHistory : public BaseAction {
    void PrintWatchHistory::act (Session& sess){
    sess.add_actionlog(*this);
    std::vector<Watchable*>& history =  sess.get_activeUser().get_history();
    cout<<"Watch history for " + sess.get_activeUser().getName()<<endl;
    if (history.empty())
        cout<< "There is no watching history"<<endl;
    else {
        int index = 1;
        for (auto h: history) {
            cout << to_string(index++) +". " + h->toString()<< endl;
        }
    }
    complete();
    }

    std::string PrintWatchHistory::toString() const{
        string s = "PrintWatchHistory "  + getStatusString(getStatus());
        if (get_errorMsg()!="")
            s+=":" + get_errorMsg();
        return s;
    }

BaseAction *PrintWatchHistory::clone() {
    PrintWatchHistory* newAction = new PrintWatchHistory();
    newAction->set_status(this->getStatus());
    newAction->set_errorMsg(this->get_errorMsg());
    return newAction ;
}


//class Watch : public BaseAction {
    void Watch::act(Session& sess){
    sess.add_actionlog(*this);
    std::vector<std::string> command = sess.get_command();

    if ((int)command.size()<2)
        error("The command is too short for the action");
    else {
        stringstream id_s(command.at(1));
        int id = 0;
        try { id_s >> id; }
        catch (const std::exception &e) { error("please choose a correct Id number"); }
        if (id >= (int)(sess.get_content().size())) {
            error("Id out of bounds");
        }
        else {
            cout << "Watching " + sess.get_content().at(id)->toString() << endl; // print watching on screen
            sess.get_activeUser().get_history().push_back(sess.get_content().at(id)); // add to history
            Watchable* recommend = sess.get_content().at(id)->getNextWatchable(sess);
            if(recommend== nullptr){
                error(("already watched everything on splflix"));
            }
            else {
                cout << "We recommend watching " + recommend->toString() + ", continue watching ? [y/n]" << endl;
                string input;
                std::getline(std::cin, input); // waits for y/n
                if (input == "y") {
                    BaseAction *action = new Watch();
                    command.at(1) = to_string(recommend->get_id());
                    sess.set_command(command);

                    complete();
                    action->act(sess);
                }
                complete();
            }
        }
    }
    }
    std::string Watch::toString() const{
        string s = "Watch "  + getStatusString(getStatus());
        if (get_errorMsg()!="")
            s+=":" + get_errorMsg();
        return s;
    }

BaseAction *Watch::clone() {
    Watch* newAction = new Watch();
    newAction->set_status(this->getStatus());
    newAction->set_errorMsg(this->get_errorMsg());
    return newAction ;
}


//class PrintActionsLog : public BaseAction {
    void PrintActionsLog::act(Session& sess){
    for(auto v: sess.get_actionsLog())
    {
        cout<<v->toString()<<endl;
    }

}
    std::string PrintActionsLog::toString() const{
        string s = "PrintActionsLog "  + getStatusString(getStatus());
        if (get_errorMsg()!="")
            s+=":" + get_errorMsg();
        return s;
    }

BaseAction *PrintActionsLog::clone() {
    PrintActionsLog* newAction = new PrintActionsLog();
    newAction->set_status(this->getStatus());
    newAction->set_errorMsg(this->get_errorMsg());
    return newAction ;
}


//class Exit : public BaseAction {
    void Exit::act(Session& sess){}
    std::string Exit::toString() const{
        string s = "Exit "  + getStatusString(getStatus());
        if (get_errorMsg()!="")
            s+=":" + get_errorMsg();
        return s;
    }

BaseAction *Exit::clone() {
    Exit* newAction = new Exit();
    newAction->set_status(this->getStatus());
    newAction->set_errorMsg(this->get_errorMsg());
    return newAction ;
}


