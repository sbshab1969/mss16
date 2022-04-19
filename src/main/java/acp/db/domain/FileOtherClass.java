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
@Table( name = "mss_logs" )
@SequenceGenerator(name = "mssl_gen", sequenceName = "mssl_seq", allocationSize = 1)
public class FileOtherClass {
  @Id
  @Column (name="mssl_id")  
  @GeneratedValue(strategy = SEQUENCE, generator="mssl_gen")
  private Long id;

  @Column (name="mssl_dt_event")  
  private Timestamp dateEvent;
  
  @Column (name="mssl_desc")  
  private String descr;
  
  @Column (name="mssl_ref_id")  
  private Long refId;
  
  @Column (name="mssl_msso_id")  
  private Long confId;
  
  @Column (name="mssl_mssc_id")  
  private Long constId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Timestamp getDateEvent() {
    return dateEvent;
  }

  public void setDateEvent(Timestamp dateEvent) {
    this.dateEvent = dateEvent;
  }

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  public Long getRefId() {
    return refId;
  }

  public void setRefId(Long refId) {
    this.refId = refId;
  }

  public Long getConfId() {
    return confId;
  }

  public void setConfId(Long confId) {
    this.confId = confId;
  }

  public Long getConstId() {
    return constId;
  }

  public void setConstId(Long constId) {
    this.constId = constId;
  }

}
