package com.carrental.service;

import com.carrental.dao.jdbc.JdbcAuthRepository;
import com.carrental.exception.ValidationException;
import com.carrental.model.UserSession;
import com.carrental.util.PasswordUtil;
import java.sql.SQLException;
import java.util.Locale;
import java.util.regex.Pattern;

public class AuthService {
    private static final Pattern EMAIL=Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern PHONE=Pattern.compile("^[+0-9() .-]{7,30}$");
    private final JdbcAuthRepository repo;
    public AuthService(JdbcAuthRepository repo){this.repo=repo;}
    public UserSession login(String email,String password)throws SQLException {if(email==null||!EMAIL.matcher(email.trim()).matches()||password==null||password.isBlank())throw new ValidationException("Enter a valid email address and password.");String normalized=email.trim().toLowerCase(Locale.ROOT);String hash=repo.passwordHash(normalized);if(!PasswordUtil.matches(password,hash))throw new ValidationException("The email or password is incorrect.");return repo.authenticate(normalized);}
    public void register(String fullName,String email,String phone,String password,String confirm)throws SQLException {
        String n=fullName==null?"":fullName.trim(),e=email==null?"":email.trim().toLowerCase(Locale.ROOT),ph=phone==null?"":phone.trim();
        if(n.length()<2||n.length()>120)throw new ValidationException("Enter a name between 2 and 120 characters.");if(!EMAIL.matcher(e).matches()||e.length()>190)throw new ValidationException("Enter a valid email address.");if(!PHONE.matcher(ph).matches())throw new ValidationException("Enter a valid phone number.");if(password==null||password.length()<8||password.length()>72)throw new ValidationException("Password must contain 8 to 72 characters.");if(!password.equals(confirm))throw new ValidationException("Passwords do not match.");
        try{repo.register(n,e,ph,PasswordUtil.hash(password));}catch(SQLException ex){if("23000".equals(ex.getSQLState()))throw new ValidationException("An account already exists for that email address.");throw ex;}
    }
    public void updateProfile(long id,String name,String phone)throws SQLException {if(name==null||name.trim().length()<2||name.trim().length()>120)throw new ValidationException("Enter a name between 2 and 120 characters.");if(phone==null||!PHONE.matcher(phone.trim()).matches())throw new ValidationException("Enter a valid phone number.");repo.updateProfile(id,name.trim(),phone.trim());}
    public void changePassword(long userId,String oldPassword,String newPassword,String confirm)throws SQLException {if(!repo.verifyPassword(userId,oldPassword))throw new ValidationException("Current password is incorrect.");if(newPassword==null||newPassword.length()<8||newPassword.length()>72)throw new ValidationException("New password must contain 8 to 72 characters.");if(!newPassword.equals(confirm))throw new ValidationException("New passwords do not match.");repo.changePassword(userId,PasswordUtil.hash(newPassword));}
}
