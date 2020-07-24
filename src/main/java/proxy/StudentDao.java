package proxy;

public interface StudentDao {
    public void add(String name);
    public int del(int id);
    public String update(String name,String id);
    public String sel(int id);
}
