package cn.com.tojob.util;



/**
 * 描述数据库分页信息的Java类
 * 
 * @version $Revision$
 * 
 * @since 2008-2-26
 */
public class Page {
	/*public static void main(String[] args) {
		Page page = new Page(47, 10);
		page.setPage(1);
		System.out.println(page.getFirstItemPos());
		System.out.println(page.getMaxItemNum());
	}*/

	/** 总共的页数 */
	private int totalPage;

	/** 总共有多少记录 */
	private long resultCount;

	/** 目前的页数 */
	private int page;

	/** 每页显示条目数 */
	private int pageSize;

	/** 要排序的字段 */
	private String orderBy;

	/** 按什么排序，只能是：asc||desc */
	private String order;

	/** 查询条件，本条件由调用该类的方法注入SearchCondition实例 */
	//private SearchCondition searchCondition = null;

	/**
	 * 分页信息Java类的构造函数
	 * 
	 * @param resultCount
	 *            总记录数
	 * @param pageSize
	 *            每页显示条目数
	 */
	public Page(long resultCount, int pageSize) {

		if (resultCount > 0) {
			this.resultCount = resultCount;
		}
		if (pageSize > 0) {
			this.pageSize = pageSize;
		}
		if (resultCount > 0 && pageSize > 0) {
			this.totalPage = (int) ((resultCount + pageSize - 1) / pageSize);
		}
		this.page = 1;
	}


	/**
	 * 分页信息Java类的构造函数
	 * 
	 * @param resultCount
	 *            总记录数
	 *            默认10
	 */
	public Page(long resultCount) {
		pageSize =10;
		if (resultCount > 0) {
			this.resultCount = resultCount;
		}
		if (resultCount > 0 && pageSize > 0) {
			this.totalPage = (int) ((resultCount + pageSize - 1) / pageSize);
		}
		this.page = 1;
	}

	/**
	 * 获得当前页的前一页，如果当前页是第一页，返回当前页。
	 * 
	 * @return 当前页的前一页
	 */
	public int getPreviousPage() {
		if (this.page - 1 <= 0) {
			return 1;
		}
		else {
			return (this.page - 1);
		}
	}

	/**
	 * 获得当前页的下一页，如果当前页是最后一页，返回当前页。
	 * 
	 * @return 当前页的下一页
	 */
	public int getNextPage() {
		if (this.page + 1 >= totalPage) {
			return totalPage;
		}
		else {
			return (this.page + 1);
		}
	}

	/**
	 * 分页查询的起始位置
	 * 
	 * @return 分页查询的起始位置
	 */
	public int getFirstItemPos() {
		int temp = (page - 1) * pageSize;
		return temp;
	}

	/**
	 * 需要从起始位置开始向后查询的总记录条数，由于分页原因，最后一页记录数不一定是每页显示的数目，可能比这小。
	 * 
	 * @return 需要从起始位置开始向后查询的总记录条数
	 */
	public long getMaxItemNum() {
		long maxItemNum = 0;
		if (resultCount <= pageSize) {
			maxItemNum = resultCount;
		}
		else if ((resultCount - (page - 1) * pageSize) >= pageSize) {
			maxItemNum = pageSize;
		}
		else {
			maxItemNum = (resultCount - (page - 1) * pageSize);
		}
		return maxItemNum;
	}

	public int getPage() {
		return page;
	}

	/**
	 * 设置当前页，如果设置的页数大于总页数，则当前页为最后一页，如果设置页数小于0，则当前页为1。
	 * 
	 * @param page
	 *            要设置的当前页
	 */
	public void setPage(int page) {
		if (page > totalPage) {
			this.page = totalPage;
		}
		else if (page <= 0) {
			this.page = 1;
		}
		else {
			this.page = page;
		}
	}
	
	/**
	 * Stirng 类型的数字转为int
	 * 设置当前页，如果设置的页数大于总页数，则当前页为最后一页，如果设置页数小于0，则当前页为1。
	 * @param StingPage
	 */

	public void setStringPage(String StingPage) {
		int page  = Integer.parseInt(StingPage);
		if (page > totalPage) {
			this.page = totalPage;
		}
		else if (page <= 0) {
			this.page = 1;
		}
		else {
			this.page = page;
		}
	}
	
	
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 获得查询条件
	 * 
	 * @return 返回 searchCondition。
	 */
	//public SearchCondition getSearchCondition() {
	//	return searchCondition;
	//}

	/**
	 * 设置查询条件，本条件由调用该类的方法注入SearchCondition实例。
	 * 
	 * @param searchCondition
	 *            要设置的 searchCondition。
	/ */
	//public void setSearchCondition(SearchCondition searchCondition) {
	//	this.searchCondition = searchCondition;
	//}

	/**
	 * 获得记录数
	 * 
	 * @return 总记录数
	 */
	public long getResultCount() {
		return resultCount;
	}

	/**
	 * 获得总页数
	 * 
	 * @return 总页数
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * 获得如何排序
	 * 
	 * @return 返回 asc或者desc。
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * 设置如何排序
	 * 
	 * @param order
	 *            要设置的排序方式，asc或者desc。
	 */
	public void setOrder(String order) {
		this.order = order;
	}

	/**
	 * 获得要排序的字段
	 * 
	 * @return 返回排序字段。
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 设置要排序的字段
	 * 
	 * @param orderBy
	 *            要设置的排序字段。
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

}
