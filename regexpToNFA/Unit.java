package regexpToNFA;

import java.util.LinkedList;
/**
 * ��С����Ϊ���ֵ�Ԫ����2��
 * 	1�����������������磨��a��(b)����unit.getStart()->u.getStart()
 *  2������������û�������磨a����unit.getStart()->unit.getEnd()
 *  ����Ԫ������nextUnitʱ��unit.getEnd()->unit.getNextUnit().getStart()
 *  �����Ԫ����û��nextUnit���Ҳ��������Unitʱ��unit.getEnd()->unit.getFather().getEnd()
 * @author Administrator
 *
 */
public class Unit {
	private int start;
	private int end;
	private Unit nextUnit;
	private Unit father;
	private LinkedList<Unit> innerFirstUnit =new LinkedList<Unit>();
	private String tranChar="";
	private int state=0; //0:���� 1:�հ� 2:���հ�
	public Unit(int s,int e,Unit unit){
		this.start=s;
		this.end=e;
		this.father=unit;
	}
	public int getStart(){
		return this.start;
	}
	public int getEnd(){
		return this.end;
	}
	public Unit getNextUnit(){
		return this.nextUnit;
	}
	public void setNextUnit(Unit unit){
		this.nextUnit=unit;
	}
	public LinkedList<Unit> getInnerFirstUnit(){
		return this.innerFirstUnit;
	}
	public void addInnerFirstUnit(Unit unit){
		this.innerFirstUnit.add(unit);
	}
	public String getTranChar(){
		return this.tranChar;
	}
	public void setTranChar(char ch){
		this.tranChar+=ch;
	}
	/**
	 * @return 0������ 1���հ� 2�����հ�
	 */
	public int getState(){
		return this.state;
	}
	/**
	 * @param s 0������ 1���հ� 2�����հ�
	 */
	public void setState(int s){
		this.state=s;
	}
	public Unit getFather(){
		return this.father;
	}
}
