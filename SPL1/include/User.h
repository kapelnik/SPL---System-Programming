#ifndef USER_H_
#define USER_H_

#include <vector>
#include <string>
#include <unordered_set>
#include <unordered_map>

class Watchable;
class Session;

class User{
public:
    User(const std::string& name);
    virtual Watchable* getRecommendation(Session& s) = 0;
    virtual User* clone(std::string newName)=0;
    std::string getName() const;
    std::vector<Watchable*>& get_history();
    bool searchinhistory(int id);
    virtual ~User()=default;

protected:
    std::vector<Watchable*> history;
private:
    std::string name;

};


class LengthRecommenderUser : public User {
public:
    LengthRecommenderUser(const std::string& name);
    virtual Watchable* getRecommendation(Session& s);
    User* clone(std::string newName);


private:

};

class RerunRecommenderUser : public User {
public:
    RerunRecommenderUser(const std::string& name);
    virtual Watchable* getRecommendation(Session& s);
    User* clone(std::string newName);


private:
    int towatch;
};

class GenreRecommenderUser : public User {
public:
    GenreRecommenderUser(const std::string& name);
    virtual Watchable* getRecommendation(Session& s);
    User* clone(std::string newName);
    static bool sortbysec(const std::pair<std::string,int> &a, const std::pair<std::string,int> &b);

private:
};

#endif
