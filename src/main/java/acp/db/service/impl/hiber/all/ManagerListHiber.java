package acp.db.service.impl.hiber.all;

import acp.db.service.IManagerList;

public abstract class ManagerListHiber<T> extends ManagerBaseHiber implements IManagerList<T> {

  @Override
  public void openQueryAll() {
  }  

  @Override
  public void openQueryPage() {
  }  
  
  @Override
  public void closeQuery() {
  }

}
