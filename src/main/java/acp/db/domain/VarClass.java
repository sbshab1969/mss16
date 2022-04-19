package acp.db.domain;

import static javax.persistence.GenerationType.SEQUENCE;

import java.sql.Timestamp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table( name = "mss_vars" )
@SequenceGenerator(name = "mssv_gen", sequenceName = "mssv_seq", allocationSize = 1)
public class VarClass {
  @Id
  @Column (name="mssv_id")  
  @GeneratedValue(strategy = SEQUENCE, generator="mssv_gen")
  private Long id;

  @Column (name="mssv_name")  
  private String name;

  @Column (name="mssv_type")  
  private String type;

  @Column (name="mssv_len")  
  private Integer len;
  
  @Column (name="mssv_valuen")  
  private Double valuen;
  
  @Column (name="mssv_valuev")  
  private String valuev;
  
  @Column (name="mssv_valued")  
  private Date valued;
  
  @Column (name="mssv_last_modify")  
  private Timestamp dateModify;
  
  @Column (name="mssv_owner")  
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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getLen() {
    return len;
  }

  public void setLen(Integer len) {
    this.len = len;
  }

  public Double getValuen() {
    return valuen;
  }

  public void setValuen(Double valuen) {
    this.valuen = valuen;
  }

  public String getValuev() {
    return valuev;
  }

  public void setValuev(String valuev) {
    this.valuev = valuev;
  }

  public Date getValued() {
    return valued;
  }

  public void setValued(Date valued) {
    this.valued = valued;
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

  public String toString() {
    String str = id + "/" + name + "/" + type + "/" + valuen + "/" + valuev
        + "/" + valued + "/";
    return str;
  }

}
