package com.stadtmeldeapp.DTO;

public class ProfilePictureDTO {
    private byte[] profilePicture;

    public ProfilePictureDTO() {
    }

    public ProfilePictureDTO(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }
    
}
