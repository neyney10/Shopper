package com.arielu.shopper.demo.classes;

public class Branches
{
    int branch_id ;
    int company_id ;
    String branch_address ;

    public Branches(int branch_id, int company_id, String branch_address)
    {
        this.branch_id = branch_id;
        this.company_id = company_id;
        this.branch_address = branch_address;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getBranch_address() {
        return branch_address;
    }

    public void setBranch_address(String branch_address) {
        this.branch_address = branch_address;
    }
}
