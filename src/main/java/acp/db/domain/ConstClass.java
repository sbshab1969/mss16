package acp.db.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.SequenceGenerator;

@Entity
@Table( name = "mss_const" )
@SequenceGenerator(name = "mssc_gen", sequenceName = "mssc_seq", allocationSize = 1)
public class ConstClass {
  @Id
  @Column (name="mssc_id")  
  @GeneratedValue(strategy = SEQUENCE, generator="mssc_gen")
  private Long id;

  @Column (name="mssc_name")  
  private String name;
  
  @Column (name="mssc_value")  
  private String value;

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

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}
