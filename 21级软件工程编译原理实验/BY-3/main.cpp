#include <iostream>
#include <fstream>
#include <assert.h>
using namespace std;

enum TokenType{   //规定TINY语言可能出现的标记
	ERR,NONE,     //错误，空词
	IF,THEN,ELSE,END,REPEAT,UNTIL,READ,WRITE,   //TINY的保留词
	ID,NUM,       //单词，数字
	ASSIGN,PLUS,MINUS,MULTI,DIV,LESS,LPAR,RPAR,COLON,EQ   //TINY的特殊符号
};

enum StateType{    //规定状态机的所有状态
	START,DONE,ERROR,   //开始状态，结束状态，错误状态
	INID,INNUM,INASSIGN,INCOMMENT    //单词状态，数字状态，:=符号状态，注释状态
};

int lineno = 0;  //当前代码行序号
int linepos = 0;   //当前代码行字符位置
int linesize = 0;   //当前代码行长度
bool saveflag = true;  //当前字符是否保存到当前识别字符串标志
string ans = "";  //当前识别字符串
string line;  //当前代码行
StateType state = START;  //当前状态机状态
TokenType token;  //当前TINY标记

bool isID(char c){    //识别一个字符是否为字母
	if((c >= 'a' && c <= 'z')||(c >= 'A' && c <= 'Z'))
	return true;
	return false;
}

bool isNUM(char c){   //识别一个字符是否为数字
	if(c >= '0' && c <= '9')
	return true;
	return false;
}

bool isOperator(char c){   //识别一个字符是否为TINY特殊运算符(除了:=)
	if(c == '+'||c == '-'||c == '*'||c == '/'||c == '='||c == '<'||c == '('||c == ')'||c == ';')
	return true;
	return false;
}

bool isWhiteSpace(char c){  //识别一个字符是否为空格或者缩进符
	if(c == ' ' || c == '\t')
	return true;
	return false;
}

TokenType identifyReserved(TokenType tok,string s){   //识别一个字符串是否为保留字并进行转换
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

void showWord(TokenType tok,string s){   //输出当前代码行识别出的标记以及识别字符串
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
			cout << "      " << lineno << ":  " << "reserved word: " << s << endl;break;  //以上标记为TINY保留字
		case ID:
			cout << "      " << lineno << ":  " << "ID, name= " << s << endl;break;  //标记为单词
		case NUM:
			cout << "      " << lineno << ":  " << "NUM, val= " << s << endl;break;  //标记为数字
		case ERR:
			cout << "      " << lineno << ":  " << "ERROR, error= " << s << endl;break;  //标记为错误
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
			cout << "      " << lineno << ":  " << s << endl;break;  //以上标记为TINY特殊字符
		default: break;
	}
}

void scanToken() {  //词法扫描当前代码行
	ans.clear();    //新的代码行开始初始化识别字符串
	linepos = 0;    //当前代码行字符位置初始化
	linesize = (int)line.length();   //获得当前代码行长度
	while (linepos < linesize) {  //代码行字符位置小于代码行长度时状态机不断识别字符
		saveflag = true;          //字符是否保存到当前识别字符串标志初始化
		char ch = line[linepos];  //ch存储当前字符
		switch (state) {          //状态机
			case START://开始状态
				if (isWhiteSpace(ch))
					saveflag = false;   //状态不变，字符不保存
				else if (isID(ch))
					state = INID;     //进入单词状态，字符保存
				else if (isNUM(ch))
					state = INNUM;    //进入数字状态，字符保存
				else if (ch == ':')
					state = INASSIGN;   //及进入:=符号状态，字符保存
				else if (ch == '{') {
					saveflag = false;    //进入注释状态，字符不保存
					state = INCOMMENT;
				} else if (isOperator(ch)) {
					state = DONE;    //识别TINY特殊符号，进入结束状态
					if (ch == '+')
						token = PLUS;         // +标记为PLUS
					else if (ch == '-')
						token =  MINUS;  // -标记为MINUS
					else if (ch == '*')
						token =  MULTI;  // *标记为MULTI
					else if (ch == '/')
						token =  DIV;    // /标记为DIV
					else if (ch == '=')
						token =  EQ;     // =标记为EQ
					else if (ch == '<')
						token =  LESS;   // <标记为LESS
					else if (ch == '(')
						token =  LPAR;   //（标记为LPAR
					else if (ch == ')')
						token =  RPAR;   // )标记为RPAR
					else
						token =  COLON;                // ;标记为COLON
				} else
					state = ERROR;  //其余字符进入错误状态
				break;
			case INID://单词状态
				//字符不为字母时，进入结束状态，字符位置返回，字符不保存，标记为ID
				if (!isID(ch)) {
					state = DONE;
					linepos--;
					saveflag = false;
					token = ID;
				}
				break;
			case INNUM://数字状态
				//字符不为数字时，进入结束状态，字符位置返回，字符不保存，标记为NUM
				if (!isNUM(ch)) {
					state = DONE;
					linepos--;
					saveflag = false;
					token = NUM;
				}
				break;
			case INASSIGN://:=符号状态
				if (ch == '=') {
					state = DONE;    //字符为 = 时，进入结束状态，字符保存，标记为ASSIGN
					token = ASSIGN;
				} else {
					state = ERROR;    //其余字符进入错误状态，字符位置返回，字符不保存
					linepos--;
					saveflag = false;
				}
				break;
			case INCOMMENT://注释状态
				if (ch == '}') {
					state = START;
					saveflag = false;
				} //字符为 } 时，进入开始状态，字符不保存
				else
					saveflag = false; //其余字符状态不变，字符不保存
				break;
			case ERROR://错误状态
				//字符为数字、字母、空格、缩进、特殊符号、花括号时进入结束状态，字符位置返回，字符不保存，标记为ERR
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
		linepos++; //字符位置向后偏移
		if (saveflag) //当前字符ch保存标记为真时，将ch加入识别字符串ans
			ans += ch;
		if (linepos == linesize) { //字符位置等于代码行长度，溢出
			switch (state) {
				case START:
					state = DONE;
					token = NONE;
					break;    //开始 -> 结束，标记为NONE
				case INID:
					state = DONE;
					token = ID;
					break;       //单词 -> 结束，标记为ID
				case INNUM:
					state = DONE;
					token = NUM;
					break;     //数字 -> 结束，标记为NUM
				case INASSIGN:
					state = DONE;
					token = ERR;
					break;  // :=  -> 结束，标记为ERR
				case ERROR:
					state = DONE;
					token = ERR;
					break;     //错误 -> 结束，标记为ERR
				default:
					break;
			}
		}
		// 当前为结束状态时，进入开始状态，输出当前代码行识别出的标记以及识别字符串，清空识别字符串
		if (state == DONE) {
			state = START;
			showWord(identifyReserved(token, ans), ans);
			ans.clear();
		}
	}
}

void codeLoading(){  //代码加载函数
	fstream myfile("test1.txt",ios::in);  //打开 test1.txt
	assert(myfile.is_open());   //检测 test.txt 是否正常打开
	while(!myfile.eof()){   //文件输入未检测到字符EOF结束
		line.clear();   //清空之前的代码行字符串
		getline(myfile,line);  //将当前代码行字符串(包含空格、缩进，不包含\n)赋予line
		cout << "LINE" << ++lineno << ":" << line << endl;  //输出当前代码行号及代码行字符串
		if(!line.empty())   //当前代码行字符串非空时进行词法扫描
			scanToken();
		if(myfile.eof() && state == INCOMMENT)   //当文件输入检测到字符EOF结束且状态仍为注释状态时报告 注释未完成 错误
			cout << "      " << lineno << ":  " << "ERROR, error= Incomplete comment";
	}
}

int main()
{
	codeLoading();
	return 0;
}
