package regexpToNFA;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * �ڵ㣬1Ϊ��ʼ�ڵ㣬2Ϊ�����ڵ�
 * @author Administrator
 *
 */
public class Node {
	private int value;
	/**
	 * �ڵ��ܵ���Ľڵ�Ͷ�Ӧ��ת���ַ�
	 */
	private Hashtable<Integer,LinkedList<String>> enableReachValues = new Hashtable<Integer,LinkedList<String>>();
	public Node(int v){
		this.value=v;
	}
	public int getValue(){
		return this.value;
	}
	public void addEnableReachValue(int value,String ch){
		if(enableReachValues.containsKey(value)){
			enableReachValues.get(value).add(ch);
		} else{
			LinkedList<String> strs = new LinkedList<String>();
			strs.add(ch);
			this.enableReachValues.put(value,strs);
		}
	}
	/**
	 * �ڵ��ܵ���Ľڵ��Ӧ��ת���ַ�
	 */
	public Hashtable<Integer,LinkedList<String>> getEnableReachValues(){
		return this.enableReachValues;
	}
	/**
	 * �ýڵ㾭��tranת���ܵ���Ľڵ�� id
	 * @param tran ת���ַ�
	 * @return �ܵ���Ľڵ��id
	 */
	public TreeSet<Integer> getNodeMove(String tran){
		//System.out.print("������="+tran+"=");
		TreeSet<Integer> nodes= new TreeSet<Integer>();
		Enumeration<Integer> e=enableReachValues.keys();
		while(e.hasMoreElements()){
			int i = e.nextElement();
			//System.out.print("�����="+enableReachValues.get(i)+"=");
			for(String str:enableReachValues.get(i)){
				if(str.equals(tran)){
					//System.out.print(i);
					nodes.add(i);
				}
			}
		}
		//System.out.print("ֵ��"+value+" "+nodes);
		return nodes;
	}
}