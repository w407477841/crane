package com.xingyun.equipment.crane.core.util;

import java.util.ArrayList;
import java.util.List;



/**
 * @author 王一飞
 * 
 * 返回 树
 * 
 * 实体类继承 TreeObject
 * 结果集中 只要有id 和 pid 就能帮你封你拉
 * 第一步 设置根目录 rootDirWithIds/rootDirWithPids/rootDir
 * 第二步 递归设置children
 */
public class TreeUtils<T extends TreeI> {
	
	/**
	 * 使用 id+pid方式
	 */
	public static final String USE_ID = "use_id"  ;  
	
	/**
	 * 使用code + pcode 方式 
	 */
	public static final String USE_CODE = "use_code"  ;		
			
	/**
	 * 根目录
	 */
	private  List<T> base=new ArrayList<>();
	
	/**
	 * 所有    非tree
	 */
	private  List<T> list=new ArrayList<>();
	
	/**
	 * 是否已经 设置过root
	 */
	private boolean [] isDoRoot ={false,false,false};
	
	
	
	/**
	 * 通过 使用code + pcode 方式
	 * 参数为 根目录的 code[]
	 * 直接返回 参数组成的数组对象
	 */
	
		public List<T> showTreeByCodes(List<T> origins,String ... codes){
			//定义返回的集合
			List<T>  results =new ArrayList<>();
			
			//找出根节点
			for(String code :codes){
				//参数不是空字符
				if(code!=null&&!"".equals(code)){
					for(T t : origins){
						if(code.equals(t.getCode())){
							base.add(t);
							//添加子节点
							t.setChildren(getChilds(origins,t));
							results.add(t);
						}
					}
				}
			}
			//根节点组装完毕
			return results;
		}
	/**
	 * 递归 子节点
	 */
	private List<T> getChilds(List<T> origins,T parent){
		List<T> childs = new ArrayList<>();
		for(T curr : origins){
			if(parent.getCode().equals(curr.getPcode())){
				list.add(curr);
				curr.setChildren(getChilds(origins,parent));
				childs.add(curr);
			}
		}
		return childs;
	}
		
		
	/**
	 *  {id:1,pid:0,label:1},{id:2,pid:0,label:2},{id:3,pid:1,label:3}
	 *  传入  1,2返回  {id:1,pid:0,children:[{id:3,pid:1,label:3}]},{id:2,pid:0,label:2}
	 * 已知 根目录ID的情况
	 */
	public void rootDirWithIds(List<T> origins  , Integer ... rootIds ){
		//生成根节点
		base =new ArrayList<>();
			for(T t :origins){
			for(Integer rootId: rootIds){
				if(t.getId().equals(rootId)){
					base.add(t);
					break;
				 }	
				}
		}
		doList(origins);
	}
	/**
	 *  {id:1,pid:0,label:1},{id:2,pid:0,label:2},{id:3,pid:1,label:3}
	 *  传入 0 返回  {id:1,pid:0,children:[{id:3,pid:1,label:3}]},{id:2,pid:0,label:2}
	 * 已知 根目录PID的情况
	 */
	public void rootDirWithPids(List<T> origins  , Integer ... rootPIds ){
		isDoRoot[1] = true;
		base =new ArrayList<>();
		if(rootPIds==null||rootPIds.length==0){
			rootDir(origins);// 可能有隐藏BUG   
		}
		else{
			for(T t :origins){
			for(Integer rootPId: rootPIds){// 
				if(t.getPid().equals(rootPId)){
					base.add(t);
					break;
				 }	
				}
			}
		}
		
		doList(origins);
	}
	/**
	 * 自动 分装
	 * @param origins
	 */
	public void rootDir(List<T> origins){
		isDoRoot[2] = true;
		//存放 已经比较过的 list
		List<T> hasComp = new ArrayList<>();
		base = new ArrayList<>();
		for( T t1 : origins){
			boolean isRoot=true;
			for(T t2:origins ){
				if(t1.getPid().equals(t2.getId())){
					isRoot = false;
					break;
				}
			}
			if(isRoot){
			base.add(t1);
			}
			
		}

		doList(origins);
		
	}
	
	private void doList(List<T> origins){
		for(  T t : base  ){//迭代根节点
			  getChildsList(origins , t  );
			}
	}
	
	public List<T>  getTreeList(){
		return base;
		
	}
	/**
	 * 返回 没有数级关系的数据
	 * @param hasBase 是否需要父级
	 * @return
	 */
	public List<T>  getList(boolean hasBase){
		List<T> result = new ArrayList<>();
		if(hasBase){
			for(T t : base){
				result.add(t);
			}
		}
		result.addAll(list);
		return result;
	}
	
	private void  getChildsList( List<T> origins , T parent){
		List<T> childs = new ArrayList<>();
		for( T t:  origins){
			if(parent.getId().equals(t.getPid())){
				 getChildsList(origins, t);
				childs.add(t);
				list.add(t);
			}
		}
		
		parent.setChildren(childs);
		
	}

}




