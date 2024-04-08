package uow.concurrent.cw;

/**
 * Represents a Customer of the Bank
 */
public class Customer {
    private String customerId;
    private String firstName;
    private String lastName;

    private String address;
    private String contactNumber;
    private String nicNo;

    public Customer(String customerId, String firstName, String lastName) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Customer(String customerId, String firstName, String lastName, String address, String contactNumber,
                    String nicNo) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.contactNumber = contactNumber;
        this.nicNo = nicNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getFullName() {
        return firstName + ' ' + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getNicNo() {
        return nicNo;
    }

    public void setNicNo(String nicNo) {
        this.nicNo = nicNo;
    }
}