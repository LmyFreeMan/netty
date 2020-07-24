package proxy;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class StudentDaoImp implements StudentDao {
    @Override
    public void add(String name) {
       log.info("add0");
    }

    @Override
    public int del(int id) {
        log.info("del()");
        return 1;
    }

    @Override
    public String update(String name, String id) {
        log.info("update()");
        return "update-success";
    }

    @Override
    public String sel(int id) {
        log.info("sel()");
        return "sel-success";
    }
}
