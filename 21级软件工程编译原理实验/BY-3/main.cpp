#include <iostream>
#include <fstream>
#include <assert.h>
using namespace std;

enum TokenType{   //�涨TINY���Կ��ܳ��ֵı��
	ERR,NONE,     //���󣬿մ�
	IF,THEN,ELSE,END,REPEAT,UNTIL,READ,WRITE,   //TINY�ı�����
	ID,NUM,       //���ʣ�����
	ASSIGN,PLUS,MINUS,MULTI,DIV,LESS,LPAR,RPAR,COLON,EQ   //TINY���������
};

enum StateType{    //�涨״̬��������״̬
	START,DONE,ERROR,   //��ʼ״̬������״̬������״̬
	INID,INNUM,INASSIGN,INCOMMENT    //����״̬������״̬��:=����״̬��ע��״̬
};

int lineno = 0;  //��ǰ���������
int linepos = 0;   //��ǰ�������ַ�λ��
int linesize = 0;   //��ǰ�����г���
bool saveflag = true;  //��ǰ�ַ��Ƿ񱣴浽��ǰʶ���ַ�����־
string ans = "";  //��ǰʶ���ַ���
string line;  //��ǰ������
StateType state = START;  //��ǰ״̬��״̬
TokenType token;  //��ǰTINY���

bool isID(char c){    //ʶ��һ���ַ��Ƿ�Ϊ��ĸ
	if((c >= 'a' && c <= 'z')||(c >= 'A' && c <= 'Z'))
	return true;
	return false;
}

bool isNUM(char c){   //ʶ��һ���ַ��Ƿ�Ϊ����
	if(c >= '0' && c <= '9')
	return true;
	return false;
}

bool isOperator(char c){   //ʶ��һ���ַ��Ƿ�ΪTINY���������(����:=)
	if(c == '+'||c == '-'||c == '*'||c == '/'||c == '='||c == '<'||c == '('||c == ')'||c == ';')
	return true;
	return false;
}

bool isWhiteSpace(char c){  //ʶ��һ���ַ��Ƿ�Ϊ�ո����������
	if(c == ' ' || c == '\t')
	return true;
	return false;
}

TokenType identifyReserved(TokenType tok,string s){   //ʶ��һ���ַ����Ƿ�Ϊ�����ֲ�����ת��
	if(s == "if") return IF;
	else if(s == "else") return ELSE;
	else if(s == "then") return THEN;
	else if(s == "end") return END;
	else if(s == "repeat") return REPEAT;
	else if(s == "until") return UNTIL;
	else if(s == "read") return READ;
	else if(s == "write") return WRITE;
	else return tok;
}

void showWord(TokenType tok,string s){   //�����ǰ������ʶ����ı���Լ�ʶ���ַ���
	if(tok == NONE)
		return;
	switch(tok){
		case IF:
		case THEN:
		case ELSE:
		case END:
		case REPEAT:
		case UNTIL:
		case READ:
		case WRITE:
			cout << "      " << lineno << ":  " << "reserved word: " << s << endl;break;  //���ϱ��ΪTINY������
		case ID:
			cout << "      " << lineno << ":  " << "ID, name= " << s << endl;break;  //���Ϊ����
		case NUM:
			cout << "      " << lineno << ":  " << "NUM, val= " << s << endl;break;  //���Ϊ����
		case ERR:
			cout << "      " << lineno << ":  " << "ERROR, error= " << s << endl;break;  //���Ϊ����
		case ASSIGN:
		case PLUS:
		case MINUS:
		case MULTI:
		case DIV:
		case LESS:
		case LPAR:
		case RPAR:
		case COLON:
		case EQ:
			cout << "      " << lineno << ":  " << s << endl;break;  //���ϱ��ΪTINY�����ַ�
		default: break;
	}
}

void scanToken() {  //�ʷ�ɨ�赱ǰ������
	ans.clear();    //�µĴ����п�ʼ��ʼ��ʶ���ַ���
	linepos = 0;    //��ǰ�������ַ�λ�ó�ʼ��
	linesize = (int)line.length();   //��õ�ǰ�����г���
	while (linepos < linesize) {  //�������ַ�λ��С�ڴ����г���ʱ״̬������ʶ���ַ�
		saveflag = true;          //�ַ��Ƿ񱣴浽��ǰʶ���ַ�����־��ʼ��
		char ch = line[linepos];  //ch�洢��ǰ�ַ�
		switch (state) {          //״̬��
			case START://��ʼ״̬
				if (isWhiteSpace(ch))
					saveflag = false;   //״̬���䣬�ַ�������
				else if (isID(ch))
					state = INID;     //���뵥��״̬���ַ�����
				else if (isNUM(ch))
					state = INNUM;    //��������״̬���ַ�����
				else if (ch == ':')
					state = INASSIGN;   //������:=����״̬���ַ�����
				else if (ch == '{') {
					saveflag = false;    //����ע��״̬���ַ�������
					state = INCOMMENT;
				} else if (isOperator(ch)) {
					state = DONE;    //ʶ��TINY������ţ��������״̬
					if (ch == '+')
						token = PLUS;         // +���ΪPLUS
					else if (ch == '-')
						token =  MINUS;  // -���ΪMINUS
					else if (ch == '*')
						token =  MULTI;  // *���ΪMULTI
					else if (ch == '/')
						token =  DIV;    // /���ΪDIV
					else if (ch == '=')
						token =  EQ;     // =���ΪEQ
					else if (ch == '<')
						token =  LESS;   // <���ΪLESS
					else if (ch == '(')
						token =  LPAR;   //�����ΪLPAR
					else if (ch == ')')
						token =  RPAR;   // )���ΪRPAR
					else
						token =  COLON;                // ;���ΪCOLON
				} else
					state = ERROR;  //�����ַ��������״̬
				break;
			case INID://����״̬
				//�ַ���Ϊ��ĸʱ���������״̬���ַ�λ�÷��أ��ַ������棬���ΪID
				if (!isID(ch)) {
					state = DONE;
					linepos--;
					saveflag = false;
					token = ID;
				}
				break;
			case INNUM://����״̬
				//�ַ���Ϊ����ʱ���������״̬���ַ�λ�÷��أ��ַ������棬���ΪNUM
				if (!isNUM(ch)) {
					state = DONE;
					linepos--;
					saveflag = false;
					token = NUM;
				}
				break;
			case INASSIGN://:=����״̬
				if (ch == '=') {
					state = DONE;    //�ַ�Ϊ = ʱ���������״̬���ַ����棬���ΪASSIGN
					token = ASSIGN;
				} else {
					state = ERROR;    //�����ַ��������״̬���ַ�λ�÷��أ��ַ�������
					linepos--;
					saveflag = false;
				}
				break;
			case INCOMMENT://ע��״̬
				if (ch == '}') {
					state = START;
					saveflag = false;
				} //�ַ�Ϊ } ʱ�����뿪ʼ״̬���ַ�������
				else
					saveflag = false; //�����ַ�״̬���䣬�ַ�������
				break;
			case ERROR://����״̬
				//�ַ�Ϊ���֡���ĸ���ո�������������š�������ʱ�������״̬���ַ�λ�÷��أ��ַ������棬���ΪERR
				if (isNUM(ch) || isID(ch) || isWhiteSpace(ch) || isOperator(ch) || ch == ':' || ch == '{') {
					state = DONE;
					linepos--;
					saveflag = false;
					token = ERR;
				}
				break;
			default:
				break;
		}
		linepos++; //�ַ�λ�����ƫ��
		if (saveflag) //��ǰ�ַ�ch������Ϊ��ʱ����ch����ʶ���ַ���ans
			ans += ch;
		if (linepos == linesize) { //�ַ�λ�õ��ڴ����г��ȣ����
			switch (state) {
				case START:
					state = DONE;
					token = NONE;
					break;    //��ʼ -> ���������ΪNONE
				case INID:
					state = DONE;
					token = ID;
					break;       //���� -> ���������ΪID
				case INNUM:
					state = DONE;
					token = NUM;
					break;     //���� -> ���������ΪNUM
				case INASSIGN:
					state = DONE;
					token = ERR;
					break;  // :=  -> ���������ΪERR
				case ERROR:
					state = DONE;
					token = ERR;
					break;     //���� -> ���������ΪERR
				default:
					break;
			}
		}
		// ��ǰΪ����״̬ʱ�����뿪ʼ״̬�������ǰ������ʶ����ı���Լ�ʶ���ַ��������ʶ���ַ���
		if (state == DONE) {
			state = START;
			showWord(identifyReserved(token, ans), ans);
			ans.clear();
		}
	}
}

void codeLoading(){  //������غ���
	fstream myfile("test1.txt",ios::in);  //�� test1.txt
	assert(myfile.is_open());   //��� test.txt �Ƿ�������
	while(!myfile.eof()){   //�ļ�����δ��⵽�ַ�EOF����
		line.clear();   //���֮ǰ�Ĵ������ַ���
		getline(myfile,line);  //����ǰ�������ַ���(�����ո�������������\n)����line
		cout << "LINE" << ++lineno << ":" << line << endl;  //�����ǰ�����кż��������ַ���
		if(!line.empty())   //��ǰ�������ַ����ǿ�ʱ���дʷ�ɨ��
			scanToken();
		if(myfile.eof() && state == INCOMMENT)   //���ļ������⵽�ַ�EOF������״̬��Ϊע��״̬ʱ���� ע��δ��� ����
			cout << "      " << lineno << ":  " << "ERROR, error= Incomplete comment";
	}
}

int main()
{
	codeLoading();
	return 0;
}
