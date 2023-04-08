package com.vmdev.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="users",schema = "public")
public class UserOld10Lesson {
    @Id
    private String username;
    private String firstname;
    private String lastname;

    //    @Convert(converter = BirthDateConvertor.class)
    @Column(name = "birth_date")
    private BirthDay birthDate;



    //    JsonBinaryType jsonBinaryType = new JsonBinaryType();
//com.vladmihalcea.hibernate.type.json.JsonBinaryType
    @JdbcTypeCode(SqlTypes.JSON)
    private String info;

    @Enumerated(EnumType.STRING)
    private Role role;

}

