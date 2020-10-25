#ifndef SESSION_H_
#define SESSION_H_

#include <vector>
#include <unordered_map>
#include <string>
#include "../include/Action.h"

class Watchable;
class User;

class Session{
public:
    Session(const std::string &configFilePath);
    ~Session();     //Destructor
    Session(const Session &other);      //Copy constructor
    Session(Session &&other);      //Move constructor
    Session& operator=(const Session &other);   //Copy Assignment           RULE OF 5
    Session& operator=(Session &&other);   //Move Assignment
    void set_activeUser(std::string name);
    void delete_user(std::string name);
    void start();
    std::vector<BaseAction*> &get_actionsLog();
    void print_actionlog();
    std::vector<Watchable*> &get_content();
    void add_user(User *user);
    void add_actionlog(BaseAction &action);
    bool isTaken(std::string name);
    User& get_activeUser();

    User* get_user_by_name(std::string name);
    std::vector<std::string> split(std::string str);
    std::vector<std::string> get_command(); // inputs separate by words
    void set_command(std::vector<std::string> cmd);
private:
    std::vector<std::string> command; // inputs separate by words
    std::vector<Watchable*> content;
    std::vector<BaseAction*> actionsLog;
    std::unordered_map<std::string,User*> userMap;
    User* activeUser;

};
#endif
