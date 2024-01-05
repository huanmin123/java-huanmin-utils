package com.utils.common.base;

import java.io.Serializable;
import java.util.List;

/**
 * 数据分页
 * @author  胡安民
 * @param <T>
 */
public class DataPaging<T> implements Serializable{

	// 页数（第几页）
	private long currentpage;

	// 查询数据库里面对应的数据有多少条
	private long total;// 从数据库查处的总记录数

	// 每页查几条
	private int size;

	// 下页
	private int next;

	//当前页的 数据
	private List<T> list;

	// 最后一页
	private int last;

	//起点
	private int lpage;
	//终点
	private int rpage;
	
	//从哪条开始查
	private long start;


	final private int offsize=2 ;  // 固定别动
	
	public DataPaging() {
		super();
	}

	/****
	 *
	 * @param currentpage  当前页
	 * @param total  总页数
	 * @param pagesize   每页显示多少条
	 */
	public void setCurrentpage(long currentpage,long total,long pagesize) {

		//如果整除表示正好分N页，如果不能整除在N页的基础上+1页
		int totalPages = (int) (total%pagesize==0? total/pagesize : (total/pagesize)+1);

		//总页数
		this.last = totalPages;

		//判断当前页是否越界,如果越界，我们就查最后一页
		if(currentpage>totalPages){
			this.currentpage = totalPages;
		}else{
			this.currentpage=currentpage;
		}
		//判断当前页是否小于1 如果小于那么就是1
		if(currentpage<1){
			this.currentpage = 1;
		}

		//计算start
		this.start = (this.currentpage-1)*pagesize;
	}

	//上一页
	public long getUpper() {
		return currentpage>1? currentpage-1: currentpage;
	}

	//总共有多少页，即末页
	public void setLast(int last) {
		this.last = (int) (total%size==0? total/size : (total/size)+1);
	}


	/****
	 * 数据库分页  ,求出分页后的信息
	 * @param total   总记录数
	 * @param currentpage	当前页
	 * @param pagesize	每页显示多少条
	 */
	public DataPaging(long total, int currentpage, int pagesize) {
		initPage(total,currentpage,pagesize);
	}

	/**
	 * 传入全部数据自动分页
	 *
	 * @param currentpage  当前页
	 * @param pagesize 每页显示多少条
	 * @param list 数据库查询的全部数据
	 */
	public DataPaging(int currentpage, int pagesize, List<T> list) {
		initPage(list,currentpage,pagesize);
	}

	/****
	 * 初始化分页  偏移量方式
	 * @param total 总记录数
	 * @param currentpage 当前页
	 * @param pagesize 每页显示多少条
	 */
	public  void initPage(long total,int currentpage,int pagesize){
		//总记录数
		this.total = total;
		//每页显示多少条
		this.size=pagesize;

		//计算当前页和数据库查询起始值以及总页数
		setCurrentpage(currentpage, total, pagesize);

		//分页计算
		int leftcount =this.offsize;	//需要向上一页执行多少次
		int rightcount =this.offsize;

		//起点页
		this.lpage =currentpage;
		//结束页
		this.rpage =currentpage;

		//2点判断
		this.lpage = currentpage-leftcount;			//正常情况下的起点
		this.rpage = currentpage+rightcount;		//正常情况下的终点

		//页差=总页数和结束页的差
		int topdiv = this.last-rpage;				//判断是否大于最大页数

		/***
		 * 起点页
		 * 1、页差<0  起点页=起点页+页差值
		 * 2、页差>=0 起点和终点判断
		 */
		this.lpage=topdiv<0? this.lpage+topdiv:this.lpage;

		/***
		 * 结束页
		 * 1、起点页<=0   结束页=|起点页|+1
		 * 2、起点页>0    结束页
		 */
		this.rpage=this.lpage<=0? this.rpage+(this.lpage*-1)+1: this.rpage;

		/***
		 * 当起点页<=0  让起点页为第一页
		 * 否则不管
		 */
		this.lpage=this.lpage<=0? 1:this.lpage;

		/***
		 * 如果结束页>总页数   结束页=总页数
		 * 否则不管
		 */
		this.rpage=this.rpage>last? this.last:this.rpage;
	}

	public void initPage(List<T> list,int currentpage,int pagesize){
		//总记录数
		this.total = list.size();
		//每页显示多少条
		this.size=pagesize;

		//计算当前页和数据库查询起始值以及总页数
		setCurrentpage(currentpage, total, pagesize);

		//分页计算   进行偏移 页数
		int leftcount =this.offsize;
		int	rightcount =this.offsize;

		//起点页
		this.lpage =currentpage;
		//结束页
		this.rpage =currentpage;

		//判断
		this.lpage = currentpage-leftcount;			//正常情况下的起点
		this.rpage = currentpage+rightcount;		//正常情况下的终点

		//页差=总页数和结束页的差
		int topdiv = this.last-rpage;				//判断是否大于最大页数

		/***
		 * 起点页
		 * 1、页差<0  起点页=起点页+页差值
		 * 2、页差>=0 起点和终点判断
		 */
		this.lpage=topdiv<0? this.lpage+topdiv:this.lpage;

		/***
		 * 结束页
		 * 1、起点页<=0   结束页=|起点页|+1
		 * 2、起点页>0    结束页
		 */
		this.rpage=this.lpage<=0? this.rpage+(this.lpage*-1)+1: this.rpage;

		/***
		 * 当起点页<=0  让起点页为第一页
		 * 否则不管
		 */
		this.lpage=this.lpage<=0? 1:this.lpage;

		/***
		 * 如果结束页>总页数   结束页=总页数
		 * 否则不管
		 */
		this.rpage=this.rpage>last? this.last:this.rpage;
		List<T>  list1=null;
		if (this.currentpage <=1) {
			if (this.total >= pagesize) {  //总条数如果大于或者等于每页显示多少条那么进行切割
				list1 = list.subList(0, this.size);
			} else {
				list1=list;
			}
		} else {

			if (this.currentpage==this.last||this.currentpage>this.last) {
				this.currentpage=this.last;
				int l = (int)this.currentpage * this.size;
				int l1 = (int)(l - this.total);
				list1 = list.subList(l-this.size,(int)this.total);
			}else{
				int l = (int)(this.currentpage-1) * this.size;
				list1 = list.subList(l , l + this.size);
			}

		}

		this.list=list1;

	}


	public long getNext() {
		return  currentpage<last? currentpage+1: last;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public long getCurrentpage() {
		return currentpage;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getLast() {
		return last;
	}

	public long getLpage() {
		return lpage;
	}

	public void setLpage(int lpage) {
		this.lpage = lpage;
	}

	public long getRpage() {
		return rpage;
	}

	public void setRpage(int rpage) {
		this.rpage = rpage;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public void setCurrentpage(long currentpage) {
		this.currentpage = currentpage;
	}

	/**
	 * @return the list
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<T> list) {
		this.list = list;
	}


}
