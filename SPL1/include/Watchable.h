#ifndef WATCHABLE_H_
#define WATCHABLE_H_

#include <string>
#include <vector>

class Session;

class Watchable{
public:
    Watchable(long id, int length, const std::vector<std::string>& tags);
    virtual ~Watchable();
    virtual std::string toString() const = 0;
    virtual Watchable* getNextWatchable(Session& s) const = 0;
    virtual std::string get_name() const=0;
    virtual std::string get_full_name() const=0;
    virtual Watchable* clone()=0;
    std::vector<std::string>  get_tags() const;
    long get_id() const;
    int get_length() const;
private:
    const long id;
    int length;
    std::vector<std::string> tags;
};




class Movie : public Watchable{
public:
    Movie(long id, const std::string& name, int length, const std::vector<std::string>& tags);
    virtual std::string toString() const;
    virtual Watchable* getNextWatchable(Session& s) const;
    virtual std::string get_name() const;
    virtual std::string get_full_name() const;
    virtual Watchable* clone();
private:
    std::string name;
};



class Episode: public Watchable{
public:
    Episode(long id, const std::string& seriesName,int length, int season, int episode ,const std::vector<std::string>& tags);
    virtual std::string toString() const;
    virtual Watchable* getNextWatchable(Session& s)  const;
    virtual std::string get_name() const;
    virtual std::string get_full_name() const;
    virtual Watchable* clone();
    int get_season() const;
    int get_episode() const;

private:
    std::string seriesName;
    int season;
    int episode;
    long nextEpisodeId;
};



#endif
