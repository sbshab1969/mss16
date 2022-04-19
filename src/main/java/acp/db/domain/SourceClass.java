package acp.db.domain;

import static javax.persistence.GenerationType.SEQUENCE;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table( name = "mss_source" )
@SequenceGenerator(name = "msss_gen", sequenceName = "mssc_seq", allocationSize = 1)
public class SourceClass {
  @Id
  @Column (name="msss_id")  
  @GeneratedValue(strategy = SEQUENCE, generator="msss_gen")
  private Long id;
  
  @Column (name="msss_name")  
  private String name;
  
  @Column (name="msss_dt_create")  
  private Timestamp dateCreate;
  
  @Column (name="msss_dt_modify")  
  private Timestamp dateModify;
  
  @Column (name="msss_owner")  
  private String owner;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Timestamp getDateCreate() {
    return dateCreate;
  }

  public void setDateCreate(Timestamp dateCreate) {
    this.dateCreate = dateCreate;
  }

  public Timestamp getDateModify() {
    return dateModify;
  }

  public void setDateModify(Timestamp dateModify) {
    this.dateModify = dateModify;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

}
