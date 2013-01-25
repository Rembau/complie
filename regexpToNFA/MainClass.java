package regexpToNFA;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.TreeSet;

import NFAToDNA.*;

/**
 * @author ľľ��ï��
 * ����ʵ���˴�������ʽ��NFA��ת��
 * ���ڣ�2012.4.14
 */		
public class MainClass {
	private String regexp;
	private int note=1;
	private Unit firstUnit;
	private LinkedList<Unit> unitList = new LinkedList<Unit>();//���е�Ԫ
	private LinkedList<Transfer> transferList = new LinkedList<Transfer>();  //
	private Hashtable<Integer,Node> nodeList = new Hashtable<Integer,Node>();
	private TreeSet<String> inputChar = new TreeSet<String>();
	private LinkedList<Integer> handledUnit =new LinkedList<Integer>(); //�Ѵ���ĵ�Ԫ�Ŀ�ʼint
	public MainClass(String re){
		this.regexp=re;
		this.regexp=initRegexp();
		System.out.println(this.regexp);
		handle();
		createNFA();
	}
	public Hashtable<Integer,Node> getNodeList(){
		return this.nodeList;
	}
	public TreeSet<String> getInputChar(){
		return this.inputChar;
	} 
	/**
	 * ��ʼ��������ʽ��������ţ����ֵ�Ԫ  ��(a|b)\**ab ->(((a)|(b))(\*)*(a)(b))
	 * ���ⲿ��������\w ������[a-b]
	 */
	public String initRegexp(){
		char[] words = regexp.toCharArray();
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<words.length;i++){
			if(Syntax.isKeyWord(words[i])){
				sb.append(words[i]);
			}  else if(Syntax.isThransMean(words[i])){
				if(i<words.length-1){
					sb.append('(');
					sb.append(words[i]);
					sb.append(words[i+1]);
					sb.append(')');
					i++;
				} else {
					System.out.println("����");
					System.exit(1);
				}
			} else if(Syntax.isIncludeLeft(words[i])){
				sb.append('(');
				boolean mark=true;
				do {
					if(i>=words.length){
						System.out.println("����");
						System.exit(1);
					}
					sb.append(words[i]);
					if(Syntax.isIncludeRight(words[i])){
						mark=false;
					}
					i++;
				} while(mark);
				i--;
				sb.append(')');
			} else{
				sb.append('(');
				sb.append(words[i]);
				sb.append(')');
			}
		}
		return new String(sb);
	}
	int i=0;
	public void handle(){
		handle(null,null,0,0);
	}
	/**
	 * �������е�Unit
	 * @param father����ǰ��Ԫ�ĸ���Ԫ
	 * @param brother����ǰ��Ԫ��ͬһ����ǰһ����Ԫ�磺����a����b����a����b��brother
	 * @param markNotNextUnit������Ƿ�Ӧ��ִ��brother.setNextUnit(unit);�˲���ֻ��ͬ����Ԫ��֮�����.
	 * ÿһ��unit_now��ʼ��Ϊnull,�ȵ�һ��unit��brotherΪnull,��������|��ʱ������markNotNextUnitΪ1����һ
	 * ��unit����ָ����ֵ������|�����ź���ĵ�һ��unit��Ӧ��brother
	 * @param markAddInnerFistUnit������Ƿ�Ӧ��ִ�� father.addInnerFirstUnit(unit);unit����unitʱ
	 * ��unit�еĵ�һ��unitӦ��ִ������������ֻ�������|��ʱ���˷��ŵĺ���һ��unitҲӦִ�иò���
	 * @return
	 */
	public Unit handle(Unit father,Unit brother,int markNotNextUnit,int markAddInnerFistUnit){
		int markAddInnerFistUnitInit=0;
		Unit unit_now=null;
		char ch;
		int start = getNowNum();
		int end = getNowNum();
		Unit unit= new Unit(start,end,father);
		nodeList.put(start,new Node(start));
		nodeList.put(end,new Node(end));
		unitList.add(unit);
		if(firstUnit==null){
			firstUnit = unit;
		}
		if(father !=null){
			//System.out.println("new Unit()"+unit.getStart()+" "+unit.getEnd() +"father"+father.getStart());
		} else {
			//System.out.println("new Unit()"+unit.getStart()+" "+unit.getEnd());
		}
		if(father !=null && markAddInnerFistUnit==0){
			father.addInnerFirstUnit(unit);
			//System.out.println("addInnerFirstUnit"+father.getStart()+" "+unit.getStart());
		}
		if(brother!=null && markNotNextUnit==0){
			brother.setNextUnit(unit);
			//System.out.println("brother"+brother.getStart() +" "+unit.getStart());
		}
		while(i<regexp.length()){
			ch=regexp.charAt(i);
			//System.out.println(ch);
			i++;
			if(ch=='('){
				Unit unit_1=handle(unit,unit_now,markNotNextUnit,markAddInnerFistUnitInit);
				markNotNextUnit=0;
				markAddInnerFistUnitInit=1;
				unit_now = unit_1;
			} else if(ch==')'){
				if(i<regexp.length() && regexp.charAt(i)==Syntax.closure){
					//System.out.println("�հ�"+regexp.charAt(i));
					unit.setState(1);
					i++;
				} 
				if(i<regexp.length() && regexp.charAt(i)==Syntax.positiveClosure){
					unit.setState(2);
					i++;
				}
				return unit;
			} else if(ch=='|'){
				markAddInnerFistUnitInit=0;
				markNotNextUnit=1;
			} else {
				//System.out.println(unit.getStart());
				if(ch=='\\'){
					unit.setTranChar(ch);
					unit.setTranChar(regexp.charAt(i++));
				} else {
					unit.setTranChar(ch);
				}
			}
		}
		return unit;
	}
	public int getNowNum(){
		return this.note++;
	}
	public void createNFA(){
		for(Unit unit:unitList){
			//System.out.println(unit.getStart()+"->"+unit.getEnd()+":unit.getStart()->unit.getEnd()=======");
			if(unit.getTranChar().equals("")){
				ManagerK.addUnit(new K(unit));
			}
			createNFA(unit);
		}
	}
	public void createNFA(Unit unit){
		if(handledUnit.contains(unit.getStart())){
			return;
		} else {
			handledUnit.add(unit.getStart());
		}
		if(unit.getState()==1){
			
			h(unit.getStart(),unit.getEnd());
			h(unit.getEnd(),unit.getStart());
			//System.out.println(unit.getStart()+"->"+unit.getEnd()+":unit.getStart()->unit.getEnd()");
			//System.out.println(unit.getEnd()+"->"+unit.getStart()+":unit.getEnd()->unit.getStart()");
		} else if(unit.getState()==2){
			h(unit.getEnd(),unit.getStart());
			//System.out.println(unit.getEnd()+"->"+unit.getStart()+":unit.getEnd()->unit.getStart()");
		}
		if(unit.getInnerFirstUnit().size()!=0){
			for(Unit u:unit.getInnerFirstUnit()){
				h(unit.getStart(),u.getStart());
				//System.out.println(unit.getStart()+"->"+u.getStart()+":unit.getStart()->u.getStart()");
				createNFA(u);
			}
		} else {
			h(unit.getStart(),unit.getEnd(),unit.getTranChar());
			//System.out.println(unit.getStart()+"->"+unit.getEnd()+":unit.getStart()->unit.getEnd()");
		}
			
		if(unit.getNextUnit()!=null){
			h(unit.getEnd(),unit.getNextUnit().getStart());
			//System.out.println(unit.getEnd()+"->"+unit.getNextUnit().getStart()+":unit.getEnd()->unit.getNextUnit().getStart()");
		} else if(unit.getStart()!=1 && unit.getNextUnit()==null){
			h(unit.getEnd(),unit.getFather().getEnd());
			//System.out.println(unit.getEnd()+"->"+unit.getFather().getEnd()+":unit.getEnd()->unit.getFather().getEnd()");
		}
	}
	public void h(int a,int b){
		h(a,b,"");
	}
	public void h(int a,int b,String ch){
		transferList.add(new Transfer(a,b,ch));
		addNodeOfNode(a,b,ch);
	}
	public void addNodeOfNode(int a,int b,String ch){
		nodeList.get(a).addEnableReachValue(b, ch);
		if(!ch.equals("")){
			inputChar.add(ch);
		}
	}
	public LinkedList<Transfer> getTransferList(){
		return this.transferList;
	}
	public void print(int a,int b){
		
	}
	public static void main(String[] args) {
		//System.out.println("");
		MainClass mc= new MainClass("(a|b)\\**ab");//c(ab)+
		for(Transfer t:mc.transferList){
			System.out.println(t.getStart()+"->"+t.getEnd()+":"+t.getCh());
		};//a((b|c)|d)
		Enumeration<Node> e1=mc.nodeList.elements();
		while(e1.hasMoreElements()){
			Node n=e1.nextElement();
			Enumeration<Integer> e=n.getEnableReachValues().keys();
			System.out.print(n.getValue());
			while(e.hasMoreElements()){
				Integer v = e.nextElement();
				System.out.print(" "+v+"-"+n.getEnableReachValues().get(v));
			}
			System.out.println();
		}
		for(String ch:mc.inputChar){
			System.out.print(ch+" ");
		}
		
		System.out.println(mc.inputChar);
		ToDFA td=new ToDFA(mc.nodeList,mc.inputChar);
		Enumeration<Integer> e=td.getDtrans().keys();
		while(e.hasMoreElements()){
			Integer v = e.nextElement();
			for(Dtran d:td.getDtrans().get(v)){
				System.out.println(d.getMark().getId()+" "+d.getTran()+" "+d.getD().getId()+" "+d.getNodes()+" " +
						""+d.getMark().isStart()+" "+d.getD().isEnd());
			}
		}
		ManagerK.printK();
		//new MainClass("ab").handle();
	}
}
