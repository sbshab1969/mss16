package acp.db.service.impl.hiber.hql;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.service.impl.hiber.all.ManagerListHiber;
import acp.forms.dto.FileLoadDto;
import acp.utils.*;

public class FileLoadManagerListHql extends ManagerListHiber<FileLoadDto> {
  private static Logger logger = LoggerFactory.getLogger(FileLoadManagerListHql.class);

  private String tableName;
  private String[] fields;
  private String[] headers;
  private Class<?>[] types;

  private String pkColumn;
  private Long seqId;

  private String strFields;
  private String strFrom;
  private String strAwhere;
  private String strWhere;
  private String strOrder;
  
  private String strQuery;
  private String strQueryCnt;

  private List<FileLoadDto> cacheObj = new ArrayList<>();

  public FileLoadManagerListHql() {
    tableName = "FileLoadClass";

    fields = new String[] { "id", "name", "md5", "owner", "dateWork", "recAll" };

    headers = new String[] { 
        "ID"
      , Messages.getString("Column.FileName")
      , "MD5"
      , Messages.getString("Column.Owner")
      , Messages.getString("Column.DateWork")
      , Messages.getString("Column.RecordCount") 
    };

    types = new Class<?>[] { 
        Long.class
      , String.class
      , String.class
      , String.class
      , Timestamp.class
      , int.class
    };

    pkColumn = fields[0];
    seqId = 1000L;

    strFields = QueryUtils.buildSelectFields(fields, null);
    strFrom = tableName;
    strAwhere = null;
    strWhere = strAwhere;
    strOrder = pkColumn;

    prepareQuery(null);
  }

  @Override
  public String[] getHeaders() {
    return headers;    
  }

  @Override
  public Class<?>[] getTypes() {
    return types;    
  }

  @Override
  public Long getSeqId() {
    return seqId;
  }

  @Override
  public void prepareQuery(Map<String,String> mapFilter) {
    if (mapFilter != null) {
      setWhere(mapFilter);
    } else {
      strWhere = strAwhere;
    }
    strQuery = QueryUtils.buildQuery(strFields, strFrom, strWhere, strOrder);
    strQueryCnt = QueryUtils.buildQuery("select count(*) from " + strFrom, strWhere, null);
  }

  private void setWhere(Map<String,String> mapFilter) {
    // ----------------------------------
    String vName = mapFilter.get("name");
    String vOwner = mapFilter.get("owner");
    String vDateWorkBeg = mapFilter.get("dateWorkBeg");
    String vDateWorkEnd = mapFilter.get("dateWorkEnd");
    String vRecAllBeg = mapFilter.get("recAllBeg");
    String vRecAllEnd = mapFilter.get("recAllEnd");
    // ----------------------------------
    String phWhere = null;
    String str = null;
    String vField = null;
    // ---
    if (!QueryUtils.emptyString(vName)) {
      str = "upper(name) like upper('" + vName + "%')";
      phWhere = QueryUtils.strAddAnd(phWhere, str);
    }
    // ---
    if (!QueryUtils.emptyString(vOwner)) {
      str = "upper(owner) like upper('" + vOwner + "%')";
      phWhere = QueryUtils.strAddAnd(phWhere, str);
    }
    //---
    vField = "dateWork";
    String dtBeg = null;
    String dtEnd = null;
    if (!QueryUtils.emptyString(vDateWorkBeg)) {
      dtBeg = "to_date('" + vDateWorkBeg + "','dd.mm.yyyy')";
    }
    if (!QueryUtils.emptyString(vDateWorkEnd)) {
      dtEnd = "to_date('" + vDateWorkEnd + " 23:59:59" + "','dd.mm.yyyy hh24:mi:ss')";
    }
    str = null;
    if (!QueryUtils.emptyString(dtBeg) && !QueryUtils.emptyString(dtEnd)) {
      str = vField + " between " + dtBeg + " and " + dtEnd;
    } else if (!QueryUtils.emptyString(dtBeg) && QueryUtils.emptyString(dtEnd)) {
      str = vField + " >= " + dtBeg;
    } else if (QueryUtils.emptyString(dtBeg) && !QueryUtils.emptyString(dtEnd)) {
      str = vField + " <= " + dtEnd;
    }
    if (!QueryUtils.emptyString(str)) {
      phWhere = QueryUtils.strAddAnd(phWhere, str);
    }
    //---
    vField = "recAll";
    String intBeg = null;
    String intEnd = null;
    if (!QueryUtils.emptyString(vRecAllBeg)) {
      intBeg = vRecAllBeg;
    }
    if (!QueryUtils.emptyString(vRecAllEnd)) {
      intEnd = vRecAllEnd;
    }
    str = null;
    if (!QueryUtils.emptyString(intBeg) && !QueryUtils.emptyString(intEnd)) {
      str = vField + " between " + intBeg + " and " + intEnd;
    } else if (!QueryUtils.emptyString(intBeg) && QueryUtils.emptyString(intEnd)) {
      str = vField + " >= " + intBeg;
    } else if (QueryUtils.emptyString(intBeg) && !QueryUtils.emptyString(intEnd)) {
      str = vField + " <= " + intEnd;
    }
    if (!QueryUtils.emptyString(str)) {
      phWhere = QueryUtils.strAddAnd(phWhere, str);
    }
    // ---
    strWhere = QueryUtils.strAddAnd(strAwhere, phWhere);
  }

  @Override
  public List<FileLoadDto> queryAll() {
    cacheObj = fetchPage(-1,-1);
    return cacheObj;    
  }

  @Override
  public List<FileLoadDto> fetchPage(int startPos, int cntRows) {
    Session session = dbConnect.getSession();
    Transaction tx = dbConnect.getTransaction(session);
    try { 
      // HQL -------------------------------------
      Query<?> query = session.createQuery(strQuery);
      if (startPos>0) {
        query.setFirstResult(startPos-1);  // Hibernate начинает с 0
      }
      if (cntRows>0) {
        query.setMaxResults(cntRows);
      }  
      // ==============
      fillCache(query);
      // ==============
      tx.commit();
    } catch (Exception e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
    } finally {
      dbConnect.close(session);
    }  
    return cacheObj;    
  }  

  private void fillCache(Query<?> query) {
    logger.info("\nQuery string: " + query.getQueryString());
    // ============================
    List<?> objList = query.list();
    // ============================
    cacheObj = new ArrayList<>();
    for (int i=0; i < objList.size(); i++) {
      Object[] obj = (Object[]) objList.get(i);
      cacheObj.add(getObject(obj));
    }
  }
  
  private FileLoadDto getObject(Object[] obj) {
    //---------------------------------------
    Long rsId = (Long) obj[0];
    String rsName = (String) obj[1];
    String rsMd5 = (String) obj[2];
    String rsOwner = (String) obj[3];
    Timestamp rsDateWork = (Timestamp) obj[4];
    Integer rsRecAll = (Integer) obj[5];
    //---------------------------------------
    FileLoadDto objDto = new FileLoadDto();
    objDto.setId(rsId);
    objDto.setName(rsName);
    objDto.setMd5(rsMd5);
    objDto.setOwner(rsOwner);
    objDto.setDateWork(rsDateWork);
    objDto.setRecAll(rsRecAll);
    //---------------------------------------
    return objDto;
  }

  @Override
  public long countRecords() {
    long cntRecords = 0; 
    Session session = dbConnect.getSession();
    Transaction tx = dbConnect.getTransaction(session);
    try {
      Query<?> query = session.createQuery(strQueryCnt);
      cntRecords = (Long) query.uniqueResult();
      tx.commit();
    } catch (Exception e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
    } finally {
      dbConnect.close(session);
    }  
    return cntRecords;    
  }

}