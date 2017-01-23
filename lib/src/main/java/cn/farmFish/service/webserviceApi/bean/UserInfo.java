package cn.farmFish.service.webserviceApi.bean;

/**
 * Created by Administrator on 2017/1/23.
 */

public class UserInfo {
    private String Id;
    private String CustomerNo;
    private String UserAccount;
    private String Xm;//name
    private String companyid;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCustomerNo() {
        return CustomerNo;
    }

    public void setCustomerNo(String customerNo) {
        CustomerNo = customerNo;
    }

    public String getUserAccount() {
        return UserAccount;
    }

    public void setUserAccount(String userAccount) {
        UserAccount = userAccount;
    }

    public String getXm() {
        return Xm;
    }

    public void setXm(String xm) {
        Xm = xm;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }


}
