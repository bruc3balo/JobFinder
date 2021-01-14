package com.example.jobfinder.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Models {

    public static class Job {
        private String jobTitle;
        private String jobDescription;
        private Double jobWage;
        private String jobLocation;
        private LatLng jobPosition;
        private boolean fullTime;
        private String createdAt;
        private boolean jobStatus;
        private String jobPosterUrl;

        public Job() {

        }

        public String getJobPosterUrl() {
            return jobPosterUrl;
        }

        public void setJobPosterUrl(String jobPosterUrl) {
            this.jobPosterUrl = jobPosterUrl;
        }

        public String getJobTitle() {
            return jobTitle;
        }

        public void setJobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
        }

        public String getJobDescription() {
            return jobDescription;
        }

        public void setJobDescription(String jobDescription) {
            this.jobDescription = jobDescription;
        }

        public Double getJobWage() {
            return jobWage;
        }

        public void setJobWage(Double jobWage) {
            this.jobWage = jobWage;
        }

        public String getJobLocation() {
            return jobLocation;
        }

        public void setJobLocation(String jobLocation) {
            this.jobLocation = jobLocation;
        }

        public LatLng getJobPosition() {
            return jobPosition;
        }

        public void setJobPosition(LatLng jobPosition) {
            this.jobPosition = jobPosition;
        }

        public boolean isFullTime() {
            return fullTime;
        }

        public void setFullTime(boolean fullTime) {
            this.fullTime = fullTime;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public boolean isJobStatus() {
            return jobStatus;
        }

        public void setJobStatus(boolean jobStatus) {
            this.jobStatus = jobStatus;
        }
    }

    public static class User {
        private String uid;
        public static final String UID = "uid";
        private String firstName;
        public static final String FIRST_NAME = "firstName";
        private String lastName;
        public static final String LAST_NAME = "lastName";
        private String emailAddress;
        public static final String EMAIL_ADDRESS = "emailAddress";
        private String phoneNumber;
        public static final String PHONE_NUMBER = "phoneNumber";
        private String createdAt;
        public static final String CREATED_AT = "createdAt";
        private String locatedAt;
        public static final String LOCATED_AT = "locatedAt";
        private LatLng positionLocatedAt;
        public static final String POSITION_LOCATED_AT = "positionLocatedAt";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        private List<String> specialization;
        public static final String SPECIALIZATION = "specialization";
        private String profileImageUrl;
        public static final String PROFILE_IMAGE = "profileImageUrl";
        private boolean available;
        public static final String AVAILABLE = "available";

        public static final String USER_DB = "Users";

        public static final String NO_PROFILE_IMAGE = "https://cdn.onlinewebfonts.com/svg/img_184513.png";


        public User() {
        }



        public User(String firstName) {
            this.firstName = firstName;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getLocatedAt() {
            return locatedAt;
        }

        public void setLocatedAt(String locatedAt) {
            this.locatedAt = locatedAt;
        }

        public LatLng getPositionLocatedAt() {
            return positionLocatedAt;
        }

        public void setPositionLocatedAt(LatLng positionLocatedAt) {
            this.positionLocatedAt = positionLocatedAt;
        }

        public List<String> getSpecialization() {
            return specialization;
        }

        public void setSpecialization(List<String> specialization) {
            this.specialization = specialization;
        }

        public String getProfileImageUrl() {
            return profileImageUrl;
        }

        public void setProfileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
        }
    }

    public static class Notification {
        private String notificationId;
        private String notificationTitle;
        private String notificationPostedAt;
        private String notificationContent;

        public Notification() {
        }

        public String getNotificationId() {
            return notificationId;
        }

        public void setNotificationId(String notificationId) {
            this.notificationId = notificationId;
        }

        public String getNotificationTitle() {
            return notificationTitle;
        }

        public void setNotificationTitle(String notificationTitle) {
            this.notificationTitle = notificationTitle;
        }

        public String getNotificationPostedAt() {
            return notificationPostedAt;
        }

        public void setNotificationPostedAt(String notificationPostedAt) {
            this.notificationPostedAt = notificationPostedAt;
        }

        public String getNotificationContent() {
            return notificationContent;
        }

        public void setNotificationContent(String notificationContent) {
            this.notificationContent = notificationContent;
        }
    }

    public static class Reviews {
        private String jobId;

        private String employeeReview;
        private String employeeId;
        private String employeeReviewedAt;

        private String employerReview;
        private String employerId;
        private String employerReviewedAt;

        private String completedAt;

        public Reviews() {
        }

        public String getJobId() {
            return jobId;
        }

        public void setJobId(String jobId) {
            this.jobId = jobId;
        }

        public String getEmployeeReview() {
            return employeeReview;
        }

        public void setEmployeeReview(String employeeReview) {
            this.employeeReview = employeeReview;
        }

        public String getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }

        public String getEmployeeReviewedAt() {
            return employeeReviewedAt;
        }

        public void setEmployeeReviewedAt(String employeeReviewedAt) {
            this.employeeReviewedAt = employeeReviewedAt;
        }

        public String getEmployerReview() {
            return employerReview;
        }

        public void setEmployerReview(String employerReview) {
            this.employerReview = employerReview;
        }

        public String getEmployerId() {
            return employerId;
        }

        public void setEmployerId(String employerId) {
            this.employerId = employerId;
        }

        public String getEmployerReviewedAt() {
            return employerReviewedAt;
        }

        public void setEmployerReviewedAt(String employerReviewedAt) {
            this.employerReviewedAt = employerReviewedAt;
        }

        public String getCompletedAt() {
            return completedAt;
        }

        public void setCompletedAt(String completedAt) {
            this.completedAt = completedAt;
        }
    }

}
