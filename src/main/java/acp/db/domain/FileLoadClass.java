package acp.db.domain;

import static javax.persistence.GenerationType.SEQUENCE;

import java.sql.Timestamp;

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
@Table( name = "mss_files" )
@SequenceGenerator(name = "mssf_gen", sequenceName = "mssf_seq", allocationSize = 1)
public class FileLoadClass {
  @Id
  @Column (name="mssf_id")  
  @GeneratedValue(strategy = SEQUENCE, generator="mssf_gen")
  private Long id;
  
  @Column (name="mssf_name")  
  private String name;
  
  @Column (name="mssf_md5")  
  private String md5;
  
  @Column (name="mssf_dt_create")  
  private Timestamp dateCreate;
  
  @Column (name="mssf_dt_work")  
  private Timestamp dateWork;
  
  @Column (name="mssf_owner")  
  private String owner;
  
  @Column (name="mssf_statistic.getStringVal()", insertable=false, updatable=false)  
  private String statisticR;
  
  @Column (name="mssf_statistic")  
  private String statisticW;
  
  @Column (name="mssf_msso_id")  
  private Long configId;
  
  @Column (name="mssf_rec_all")  
  private int recAll;
  
  @Column (name="mssf_rec_er")  
  private int recErr;
  
  @Column (name="mssf_sec_all")  
  private int secAll;
  
  @Column (name="mssf_sec_er")  
  private int secErr;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="mssf_msso_id", insertable=false, updatable=false)
  private ConfigClass config;

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

  public String getMd5() {
    return md5;
  }

  public void setMd5(String md5) {
    this.md5 = md5;
  }

  public Timestamp getDateCreate() {
    return dateCreate;
  }

  public void setDateCreate(Timestamp dateCreate) {
    this.dateCreate = dateCreate;
  }

  public Timestamp getDateWork() {
    return dateWork;
  }

  public void setDateWork(Timestamp dateWork) {
    this.dateWork = dateWork;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getStatisticR() {
    return statisticR;
  }

  public void setStatisticR(String statisticR) {
    this.statisticR = statisticR;
  }

  public String getStatisticW() {
    return statisticW;
  }

  public void setStatisticW(String statisticW) {
    this.statisticW = statisticW;
  }

  public Long getConfigId() {
    return configId;
  }

  public void setConfigId(Long configId) {
    this.configId = configId;
  }

  public int getRecAll() {
    return recAll;
  }

  public void setRecAll(int recAll) {
    this.recAll = recAll;
  }

  public int getRecErr() {
    return recErr;
  }

  public void setRecErr(int recErr) {
    this.recErr = recErr;
  }

  public int getSecAll() {
    return secAll;
  }

  public void setSecAll(int secAll) {
    this.secAll = secAll;
  }

  public int getSecErr() {
    return secErr;
  }

  public void setSecErr(int secErr) {
    this.secErr = secErr;
  }

  public ConfigClass getConfig() {
    return config;
  }

  public void setConfig(ConfigClass config) {
    this.config = config;
  }

}
