package com.stadtmeldeapp.DTO;

public class LoginDataDTO {
        String username;
        String password;

        public LoginDataDTO() {

        }

        public LoginDataDTO(String username, String password) {
                this.username = username;
                this.password = password;
        }

        public String getUsername() {
                return username;
        }
        public void setUsername(String username) {
                this.username = username;
        }
        public String getPassword() {
                return password;
        }
        public void setPassword(String password) {
                this.password = password;
        }

        @Override
        public String toString() {
                return "LoginData [username=" + username + ", password=" + password + "]";
        }
}
