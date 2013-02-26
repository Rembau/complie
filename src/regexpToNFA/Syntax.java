package regexpToNFA;

/**
 * �﷨�е�һЩ�ؼ���
 * @author Administrator
 *
 */
public class Syntax {
	/**
	 * �հ�
	 */
	public static char closure='*';
	/**
	 * ���հ�
	 */
	public static char positiveClosure='+';
	/**
	 * ��
	 */
	public static char or='|';
	public static char transMean='\\';
	public static char[] keyChar=new char[]{'*','+','(',')','|','(',')','?'};  //�ؼ���
	public static char includeLeft='[';
	public static char includeRight=']';
	
	public static char lastChar;
	public static char nextChar;
	public static String noPlace[]=new String[]{"\\b","\\c"};   //��ռλת�Ʒ���  \\b���ʿ�ʼ  \\d���ʽ���
	public static char keyCharForS[]=new char[]{'-'};  //����������Ĺؼ���
	
	public static boolean isNoPlace(String tran){
		for(String str:noPlace){
			if(tran.equals(str)){
				return true;
			}
		}
		return false;
	}
	public static boolean isIncludeLeft(char ch){
		return includeLeft==ch;
	}
	public static boolean isIncludeRight(char ch){
		return includeRight==ch;
	}
	/**
	 * �ж��Ƿ��� '*','+','(',')','|','(',')' �е�һ��
	 * @param str
	 * @return
	 */
	public static boolean isKeyWord(char str){
		for(int i=0;i<keyChar.length;i++){
			if(keyChar[i]==str){
				return true;
			}
		}
		return false;
	}
	/**
	 * �ж��Ƿ���ת���ַ�\
	 * @param str
	 * @return
	 */
	public static boolean isThransMean(char str){
		if(transMean==str){
			return true;
		}
		return false;
	}
	/**
	 * �ж��Ƿ�ƥ��
	 * @param tran ת���ַ� ������char������\w�ؼ���
	 * @param in �����char
	 * @return
	 */
	public static boolean isMatch(String tran,char in){
		if(tran.startsWith("\\")){
			char ch[]=tran.toCharArray();
			if(isKeyWord(ch[1]) || ch[1]=='\\' || ch[1]=='.'){
				return in==ch[1];
			} else {
				if(tran.equals("\\d")){
					return isMatchScope('0','9',in);
				} else if(tran.equals("\\D")){
					return !isMatchScope('0','9',in);
				} else if(tran.equals("\\w")){
					return isMatchScope('0','9',in) || isMatchScope('a','z',in) || 
						isMatchScope('A','Z',in) || in=='_';
				} else if(tran.equals("\\W")){
					return !(isMatchScope('0','9',in) || isMatchScope('a','z',in) || 
					isMatchScope('A','Z',in) || in=='_');
				} else if(tran.equals("\\b")){
					if(lastChar==0){
						return true;
					}
					return !(isMatchScope('0','9',lastChar) || isMatchScope('a','z',lastChar) || 
							isMatchScope('A','Z',lastChar) || lastChar=='_');
				}
				else if(tran.equals("\\c")){
					return !(isMatchScope('0','9',in) || isMatchScope('a','z',in) || 
							isMatchScope('A','Z',in) || in=='_');
				}
				return false;
			}
		} else if(tran.equals(".")){
			return true;
		} else if(tran.startsWith("[")){
			tran=tran.substring(1, tran.length()-1);
			if(tran.startsWith("^")){
				
			} else {
				
			}
			return true;
		} else {
			return tran.toCharArray()[0]==in;
		}
	}
	public static void setLastNext(char last,char next){
		lastChar = last;
		nextChar = next;
	}
	public static boolean isMatchScope(char cha[],char ch){
		for(char c:cha){
			if(c==ch){
				return true;
			}
		}
		return false;
	}
	public static boolean isMatchScope(char start,char end,char ch){
		if(ch>=start && ch<=end){
			return true;
		}
		return false;
	}
}
