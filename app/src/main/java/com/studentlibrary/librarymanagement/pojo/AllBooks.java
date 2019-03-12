package com.studentlibrary.librarymanagement.pojo;

import java.io.Serializable;

/**
 * Created by Shiva XiD on 27-10-2017.
 */

public class AllBooks implements Serializable{
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getIssueId() {
        return issueId;
    }

    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }

    private  int issueId;
    private int bookId;
    private String bookName;
    private String bookAuthor;
    private String bookPublisher;
    private String bookEdition;
    private String department;
    private String bookType;
    private String barcode;
    private int totalCopies;

    public int getBookNo() {
        return bookNo;
    }

    public void setBookNo(int bookNo) {
        this.bookNo = bookNo;
    }

    public int getRollNo() {
        return rollNo;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public String getActualDate() {
        return actualDate;
    }

    public void setActualDate(String actualDate) {
        this.actualDate = actualDate;
    }

    public String getIssuedate() {
        return issuedate;
    }

    public void setIssuedate(String issuedate) {
        this.issuedate = issuedate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    private int bookNo;
    private int rollNo;
    private String actualDate;
    private String issuedate;
    private String returnDate;
    private int fine;


//    public AllBooks(String bookName, String bookAuthor, String bookPublisher, String bookEdition, String department, String bookType, String barcode, int totalCopies) {
//        this.bookName=bookName;
//        this.bookAuthor=bookAuthor;
//        this.bookPublisher=bookPublisher;
//        this.bookEdition=bookEdition;
//        this.department=department;
//        this.bookType=bookType;
//        this.barcode=barcode;
//        this.totalCopies=totalCopies;
//    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookPublisher() {
        return bookPublisher;
    }

    public void setBookPublisher(String bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

    public String getBookEdition() {
        return bookEdition;
    }

    public void setBookEdition(String bookEdition) {
        this.bookEdition = bookEdition;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

}
