package fin.av.thesis.REST.Helper;

import fin.av.thesis.DAL.Document.UserManagement.User;

public class UserHelper {
    public static void updateUser(User existingUser, User updatedUser) {
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());
        //existingUser.setEnabled(true);
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
    }
}