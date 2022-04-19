package acp.db.service.impl.hiber.sqljpa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

//import org.hibernate.type.AbstractSingleColumnStandardBasicType;
//import org.hibernate.type.LongType;
//import org.hibernate.type.StringType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.service.impl.hiber.all.ManagerListHiber;
import acp.forms.dto.SourceDto;
import acp.utils.*;

public class SourceManagerListSqlJpa extends ManagerListHiber<SourceDto> {
  private static Logger logger = LoggerFactory.getLogger(SourceManagerListSqlJpa.class);

  private String tableName;
  private String[] fields;
  private String[] headers;
  private Class<?>[] types;
//  private AbstractSingleColumnStandardBasicType<?>[] hiberTypes;

  private String pkColumn;
  private Long seqId;

  private String strFields;
  private String strFrom;
  private String strAwhere;
  private String strWhere;
  private String strOrder;
  
  private String strQuery;
  private String strQueryCnt;

  private List<SourceDto> cacheObj = new ArrayList<>();

  public SourceManagerListSqlJpa() {
    tableName = "mss_source";

    fields = new String[] { "msss_id", "msss_name", "msss_owner" };

    headers = new String[] { 
        "ID"
      , Messages.getString("Column.Name")
      , Messages.getString("Column.Owner") 
    };

    types = new Class<?>[] { 
        Long.class
      , String.class
      , String.class
    };

//    hiberTypes = new AbstractSingleColumnStandardBasicType<?>[] { 
//        LongType.INSTANCE
//      , StringType.INSTANCE
//      , StringType.INSTANCE
//    };

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
    strQueryCnt = QueryUtils.buildQuery("select count(*) cnt from " + strFrom, strWhere, null);
  }

  private void setWhere(Map<String,String> mapFilter) {
    // ----------------------------------
    String vName = mapFilter.get("name"); 
    String vOwner = mapFilter.get("owner");;
    // ----------------------------------
    String phWhere = null;
    String str = null;
    // ---
    if (!QueryUtils.emptyString(vName)) {
      str = "upper(msss_name) like upper('" + vName + "%')";
      phWhere = QueryUtils.strAddAnd(phWhere, str);
    }
    // ---
    if (!QueryUtils.emptyString(vOwner)) {
      str = "upper(msss_owner) like upper('" + vOwner + "%')";
      phWhere = QueryUtils.strAddAnd(phWhere, str);
    }
    // ---
    strWhere = QueryUtils.strAddAnd(strAwhere, phWhere);
  }

  @Override
  public List<SourceDto> queryAll() {
    cacheObj = fetchPage(-1,-1);
    return cacheObj;    
  }

  @Override
  public List<SourceDto> fetchPage(int startPos, int cntRows) {
    EntityManager entityManager = dbConnect.getEntityManager();
    EntityTransaction tx = dbConnect.getEntityTransaction(entityManager);
    try { 
      // -------------------------------------
      Query query = entityManager.createNativeQuery(strQuery);
//      for (int i=0; i<hiberTypes.length; i++) {
//        query.addScalar(fields[i], hiberTypes[i]);
//      }
      // ------------------------------------------
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
      dbConnect.close(entityManager);
    }  
    return cacheObj;    
  }  

  private void fillCache(Query query) {
    // ============================
    List<?> objList = query.getResultList();
    // ============================
    cacheObj = new ArrayList<>();
    for (int i=0; i < objList.size(); i++) {
      Object[] obj = (Object[]) objList.get(i);
      cacheObj.add(getObject(obj));
    }
  }
  
  private SourceDto getObject(Object[] obj) {
    //---------------------------------------
//    Long rsId = (Long) obj[0];
    BigDecimal idBigDec = (BigDecimal) obj[0];
    Long rsId = idBigDec.longValue();
    
    String rsName = (String) obj[1];
    String rsOwner = (String) obj[2];
    //---------------------------------------
    SourceDto objDto = new SourceDto();
    objDto.setId(rsId);
    objDto.setName(rsName);
    objDto.setOwner(rsOwner);
    //---------------------------------------
    return objDto;
  }

  @Override
  public long countRecords() {
    long cntRecords = 0;
    EntityManager entityManager = dbConnect.getEntityManager();
    EntityTransaction tx = dbConnect.getEntityTransaction(entityManager);
    try {
      Query query = entityManager.createNativeQuery(strQueryCnt);
//              .addScalar("cnt", LongType.INSTANCE);
      BigDecimal bdCntRecords = (BigDecimal) query.getSingleResult();
      cntRecords = bdCntRecords.longValue();
      tx.commit();
    } catch (Exception e) {
      tx.rollback();
      DialogUtils.errorPrint(e, logger);
    } finally {
      dbConnect.close(entityManager);
    }
    return cntRecords;
  }

}
