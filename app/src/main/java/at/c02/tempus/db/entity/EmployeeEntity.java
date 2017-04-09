package at.c02.tempus.db.entity;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Daniel on 09.04.2017.
 */
@Entity
public class EmployeeEntity {
    @Id
    private Long id;

    private Long externalId;

    private String userName;

    private String firstName;

    private String lastName;

    @Generated(hash = 2088296668)
    public EmployeeEntity(Long id, Long externalId, String userName,
            String firstName, String lastName) {
        this.id = id;
        this.externalId = externalId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Generated(hash = 249963266)
    public EmployeeEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExternalId() {
        return this.externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


}
