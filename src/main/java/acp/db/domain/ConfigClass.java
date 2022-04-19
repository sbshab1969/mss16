package acp.db.domain;

import static javax.persistence.GenerationType.SEQUENCE;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table( name = "mss_options" )
@SequenceGenerator(name = "msso_gen", sequenceName = "msso_seq", allocationSize = 1)
public class ConfigClass {
  @Id
  @Column (name="msso_id")  
  @GeneratedValue(strategy = SEQUENCE, generator="msso_gen")
  private Long id;
  
  @Column (name="msso_name")  
  private String name;
  
  @Column (name="msso_config.getStringVal()", insertable=false, updatable=false)  
  private String configR;  // read only
  
  @Column (name="msso_config")  
  private String configW;  // write
  
  @Column (name="msso_dt_begin")  
  private Date dateBegin;
  
  @Column (name="msso_dt_end")  
  private Date dateEnd;
  
  @Column (name="msso_comment")  
  private String comment;
  
  @Column (name="msso_dt_create")  
  private Timestamp dateCreate;
  
  @Column (name="msso_dt_modify")  
  private Timestamp dateModify;
  
  @Column (name="msso_owner")  
  private String owner;
  
  @Column (name="msso_msss_id")  
  private Long sourceId;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="msso_msss_id", insertable=false, updatable=false)
  private SourceClass source;

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

  public String getConfigR() {
    return configR;
  }

  public void setConfigR(String configR) {
    this.configR = configR;
  }

  public String getConfigW() {
    return configW;
  }

  public void setConfigW(String configW) {
    this.configW = configW;
  }

  public Date getDateBegin() {
    return dateBegin;
  }

  public void setDateBegin(Date dateBegin) {
    this.dateBegin = dateBegin;
  }

  public Date getDateEnd() {
    return dateEnd;
  }

  public void setDateEnd(Date dateEnd) {
    this.dateEnd = dateEnd;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
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

  public Long getSourceId() {
    return sourceId;
  }

  public void setSourceId(Long sourceId) {
    this.sourceId = sourceId;
  }

  public SourceClass getSource() {
    return source;
  }

  public void setSource(SourceClass source) {
    this.source = source;
  }

  public String toString() {
    String str = id + "/" + name + "/" + dateBegin + "/" + dateEnd + "/"
        + comment + "/" + dateCreate + "/" + dateModify + "/" + owner + "/"
        + sourceId + "/" + configR + "/" + configW;
    return str;
  }

}
