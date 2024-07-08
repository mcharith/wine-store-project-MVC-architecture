package lk.ijse.model;

public class Supplier {
    private String supId;
    private String Name;
    private String Address;
    private String Email;
    private String Tele;

    public Supplier(){}

    public Supplier(String supId, String name, String address, String email, String tele) {
        this.supId = supId;
        Name = name;
        Address = address;
        Email = email;
        Tele = tele;
    }

    public String getSupId() {
        return supId;
    }

    public void setSupId(String supId) {
        this.supId = supId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTele() {
        return Tele;
    }

    public void setTele(String tele) {
        Tele = tele;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "supId='" + supId + '\'' +
                ", Name='" + Name + '\'' +
                ", Address='" + Address + '\'' +
                ", Email='" + Email + '\'' +
                ", Tele='" + Tele + '\'' +
                '}';
    }
}
