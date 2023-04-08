package com.vmdev.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import com.vmdev.convertor.BirthDateConvertor;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
//import org.hibernate.annotations.TypeBinderType;
//import org.hibernate.annotations.TypeRegistration;
//
//import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "username")
@ToString(exclude = "company")
@Builder
@Entity
@Table(name="users",schema = "public")
public class User {
    @Id
    @GeneratedValue(generator = "user_gen",strategy = GenerationType.IDENTITY)
// hibernate sequence created by default
    @SequenceGenerator(name = "user_gen", sequenceName = "users_id_seq", allocationSize = 1)
    private Long id;

    @Column(unique = true)
    private String username;

    @Embedded
    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    private PersonalInfo personalInfo;


//    JsonBinaryType jsonBinaryType = new JsonBinaryType();
//com.vladmihalcea.hibernate.type.json.JsonBinaryType
    @JdbcTypeCode(SqlTypes.JSON)
    private String info;

    @Enumerated(EnumType.STRING)
    private Role role;

//    @ManyToOne(optional = false, fetch = FetchType.LAZY) //optional = false - means constraint not null
    @ManyToOne( fetch = FetchType.EAGER, cascade =  CascadeType.ALL) //optional = false - means constraint not null
    @JoinColumn(name = "company_id") //company_id - on default
    private Company company;

}
