package NFAToDNA;

import java.util.TreeSet;
/**
 * @author lm
 *�½ڵ�ͼ�ı�
 */
public class Dtran {
	/**
	 * ת��ΪDNF�����У���ʼ�½ڵ��а����ľɽڵ㼯
	 */
	TreeSet<Integer> nodes = new TreeSet<Integer>(); //closure(mark.value)
	/**
	 * ��ʼ�ڵ�
	 */
	NewNode mark;
	/**
	 * ת���ַ�
	 */
	String tran;
	/**
	 * �����ڵ�
	 */
	NewNode d;
	public Dtran(TreeSet<Integer> nodes,NewNode mark,String tran,NewNode d){
		this.nodes=nodes;
		this.mark=mark;
		this.tran=tran;
		this.d=d;
	}
	public TreeSet<Integer> getNodes(){
		return this.nodes;
	}
	/**
	 * ��ʼ�ڵ�
	 */
	public NewNode getMark(){
		return this.mark;
	}
	public String getTran(){
		return this.tran;
	}
	/**
	 * �����ڵ�
	 */
	public NewNode getD(){
		return this.d;
	}
}
