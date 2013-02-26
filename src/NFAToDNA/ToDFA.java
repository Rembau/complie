package NFAToDNA;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Stack;
import java.util.TreeSet;

import regexpToNFA.Node;

/**
 * ����ľľ
 * ת��Ϊdfa���ڵ�ΪNewNode 
 */
public class ToDFA {
	/**
	 * �ڵ㼯
	 */
	private LinkedList<NewNode> newNodes=new LinkedList<NewNode>();
	private int note=1;
	/**
	 * ���е������ַ�����\w
	 */
	private TreeSet<String> input;
	/**
	 * �ɽڵ㼯���ڵ��id��Ӧ��Ӧ�Ľڵ�
	 */
	private Hashtable<Integer,Node> nodeList;
	/**
	 * �½ڵ㼯���ڵ��id��Ӧ��Ӧ�Ľڵ�
	 */
	Hashtable<Integer,LinkedList<Dtran>> dtrans = new Hashtable<Integer,LinkedList<Dtran>>();
	/**
	 * ��ʼ�ڵ�
	 */
	NewNode startNewNode;
	/**
	 * �����ڵ㼯
	 */
	LinkedList<NewNode> endNewNodes= new LinkedList<NewNode>();
	public ToDFA(Hashtable<Integer,Node> nodeList,TreeSet<String> input){
		this.input=input;
		this.nodeList=nodeList;
		handle();
		removeRedundancy();
	}
	public NewNode getStartNewNode(){
		//System.out.println("��ȡ��ʼ�ڵ�");
		return this.startNewNode;
	}
	public Hashtable<Integer,LinkedList<Dtran>> getDtrans(){
		return this.dtrans;
	}
	public LinkedList<NewNode> getNewNodes(){
		return this.newNodes;
	}
	public void handle(){
		TreeSet<Integer> nodes =new TreeSet<Integer>();
		nodes.add(nodeList.get(1).getValue());
		NewNode n=new NewNode(getNote());
		n.setNodes(closure(nodes));
		//System.out.println("========"+closure(nodes)+"===========");
		n.setStart();
		if(closure(nodes).contains(2)){
			n.setEnd();
		}
		
		newNodes.add(n);
		
		NewNode nn;
		while((nn=getNotMark())!=null){
			//System.out.println(nn.getId());
			nn.mark();
			for(String sign:input){
				TreeSet<Integer> t=closure(move(nn.getNodes(),sign));
				//System.out.println("t"+t);
				NewNode nn_=null;
				if((nn_=getNewNode(t))==null){
					nn_ = new NewNode(getNote());
					if(t.contains(1)){
						nn_.setStart();
					}
					if(t.contains(2)){
						nn_.setEnd();
						endNewNodes.add(nn_);
					}
					nn_.setNodes(t);
					newNodes.add(nn_);
				}
				addDtrans(nn.getNodes(),nn,sign,nn_);
			}
		}
		for(NewNode n_:newNodes){
			System.out.println(n_.getId()+" "+n_.getNodes());
			if(n_.isStart()){
				startNewNode = n_;
			}
		};
	}
	/**
	 * ��ȡһ��û�б���ǵ��½ڵ�
	 * @return
	 */
	public NewNode getNotMark(){
		for(NewNode nn:newNodes){
			if(!nn.getMark()){
				return nn;
			}
		}
		return null;
	}
	public void addDtrans(TreeSet<Integer> nodes,NewNode mark,String tran,NewNode d){
		if(dtrans.containsKey(mark.getId())){
			dtrans.get(mark.getId()).add(new Dtran(nodes,mark,tran,d));
		} else{
			LinkedList<Dtran> dts=new LinkedList<Dtran>();
			dts.add(new Dtran(nodes,mark,tran,d));
			dtrans.put(mark.getId(), dts);
		}
	}
	/**
	 * ȥ���ӿ�ʼ�ڵ��޷�����Ľڵ㣬�� �޷���������ڵ�Ľڵ�
	 */
	public void removeRedundancy(){
		removeNotRecFormStart();
		removeNotRecToEnd();
	}
	public void removeNotRecFormStart(){
		Stack<Integer> stack = new Stack<Integer>();
		TreeSet<Integer> r=new TreeSet<Integer>();
		r.add(this.startNewNode.getId());
		stack.push(this.startNewNode.getId());
		while(!stack.empty()){
			int key = stack.pop();
			//System.out.println("key="+key);
			for(Dtran d:dtrans.get(key)){
				if(!r.contains(d.getD().getId())){
					stack.push(d.getD().getId());
					r.add(d.getD().getId());
				}
			}
		}
		removeKey(r);
	}
	public void removeNotRecToEnd(){
		Stack<Integer> stack = new Stack<Integer>();
		TreeSet<Integer> r=new TreeSet<Integer>();
		for(NewNode nn:this.endNewNodes){
			r.add(nn.getId());
			stack.push(nn.getId());
		}
		while(!stack.empty()){
			int key = stack.pop();
			for(Dtran d:getDtransByD(key)){
				if(!r.contains(d.getMark().getId())){
					stack.push(d.getMark().getId());
					r.add(d.getMark().getId());
				}
			}
		}
		removeKey(r);
	}
	/**
	 * ���Ƴ�ĳ�ڵ�ʱ��Ӧһ���Ƴ� ʣ�����ýڵ��й�ϵ�Ľڵ�
	 * @param r
	 */
	public void removeKey(TreeSet<Integer> r){
		Enumeration<Integer> e=getDtrans().keys();
		while(e.hasMoreElements()){
			Integer v = e.nextElement();
			if(!r.contains(v)){
				//System.out.println("remove2"+v);
				dtrans.remove(v);
			}
		}
		e=getDtrans().keys();
		while(e.hasMoreElements()){
			LinkedList<Dtran> ds=new LinkedList<Dtran>();
			Integer v = e.nextElement();
			for(Dtran d:dtrans.get(v)){
				if(!r.contains(d.getD().getId())){
					ds.add(d);
				}
			}
			for(Dtran d:ds){
				dtrans.get(v).remove(d);
			}
		}
	}
	/**
	 * ��ȡ����dtran�н����ڵ�Ϊend�Ľڵ�
	 * @param end
	 * @return
	 */
	public LinkedList<Dtran> getDtransByD(int end){
		LinkedList<Dtran> ds= new LinkedList<Dtran>();
		Enumeration<Integer> e=getDtrans().keys();
		while(e.hasMoreElements()){
			Integer v = e.nextElement();
			for(Dtran d:dtrans.get(v)){
				if(d.getD().getId()==end){
					ds.add(d);
				}
			}
		}
		return ds;
	}
	/**
	 * ��ȡclosureת����õĽڵ㼯
	 * @param nodes
	 * @return
	 */
	public TreeSet<Integer> closure(TreeSet<Integer> nodes){
		TreeSet<Integer> node = new TreeSet<Integer>(); //���
		Stack<Integer> stack = new Stack<Integer>();
		for(Integer n:nodes){
			stack.push(n);
			node.add(n);
		}
		while(!stack.empty()){
			int i=stack.pop();
			//System.out.println("��ջ����ȡ"+i);
			TreeSet<Integer> n_1=nodeList.get(i).getNodeMove("");
			for(int int_1:n_1){
				if(!node.contains(int_1)){
					stack.push(int_1);
					node.add(int_1);
					//System.out.println(node);
				}
			}
		}
		return node;
	}
	/**
	 * ��ȡ��ǰ�ڵ㼯����tranת����õĽڵ㼯
	 * @param nodes
	 * @param tran
	 * @return
	 */
	public TreeSet<Integer> move(TreeSet<Integer> nodes,String tran){
		TreeSet<Integer> node = new TreeSet<Integer>();
		for(Integer n:nodes){
			node.addAll(nodeList.get(n).getNodeMove(tran));
		}
		return node;
	}
	public int getNote(){
		return note++;
	}
	public boolean isExit(TreeSet<Integer> n){
		for(NewNode nn:newNodes){
			if(nn.isSame(n)){
				return true;
			}
		}
		return false;
	}
	public NewNode getNewNode(TreeSet<Integer> n){
		for(NewNode nn:newNodes){
			if(nn.isSame(n)){
				return nn;
			}
		}
		return null;
	}
	public static void main(String[] args) {

	}
}
