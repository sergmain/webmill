// $ANTLR 2.7.3: "SqlSQL2.g" -> "SqlLexer.java"$

package tt;



import java.io.InputStream;

import antlr.TokenStreamException;

import antlr.TokenStreamIOException;

import antlr.TokenStreamRecognitionException;

import antlr.CharStreamException;

import antlr.CharStreamIOException;

import antlr.ANTLRException;

import java.io.Reader;

import java.util.Hashtable;

import antlr.CharScanner;

import antlr.InputBuffer;

import antlr.ByteBuffer;

import antlr.CharBuffer;

import antlr.Token;

import antlr.CommonToken;

import antlr.RecognitionException;

import antlr.NoViableAltForCharException;

import antlr.MismatchedCharException;

import antlr.TokenStream;

import antlr.ANTLRHashString;

import antlr.LexerSharedInputState;

import antlr.collections.impl.BitSet;

import antlr.SemanticException;



public class SqlLexer extends antlr.CharScanner implements SqlTokenTypes, TokenStream

 {

public SqlLexer(InputStream in) {

	this(new ByteBuffer(in));

}

public SqlLexer(Reader in) {

	this(new CharBuffer(in));

}

public SqlLexer(InputBuffer ib) {

	this(new LexerSharedInputState(ib));

}

public SqlLexer(LexerSharedInputState state) {

	super(state);

	caseSensitiveLiterals = false;

	setCaseSensitive(false);

	literals = new Hashtable();

	literals.put(new ANTLRHashString("round", this), new Integer(42));

	literals.put(new ANTLRHashString("initcap", this), new Integer(47));

	literals.put(new ANTLRHashString("vsize", this), new Integer(84));

	literals.put(new ANTLRHashString("all", this), new Integer(20));

	literals.put(new ANTLRHashString("sqrt", this), new Integer(44));

	literals.put(new ANTLRHashString("replace", this), new Integer(51));

	literals.put(new ANTLRHashString("count", this), new Integer(63));

	literals.put(new ANTLRHashString("nvl", this), new Integer(81));

	literals.put(new ANTLRHashString("sum", this), new Integer(67));

	literals.put(new ANTLRHashString("hextoraw", this), new Integer(71));

	literals.put(new ANTLRHashString("soundex", this), new Integer(54));

	literals.put(new ANTLRHashString("chartorowid", this), new Integer(69));

	literals.put(new ANTLRHashString("for", this), new Integer(117));

	literals.put(new ANTLRHashString("min", this), new Integer(65));

	literals.put(new ANTLRHashString("userenv", this), new Integer(83));

	literals.put(new ANTLRHashString("lower", this), new Integer(48));

	literals.put(new ANTLRHashString("sign", this), new Integer(43));

	literals.put(new ANTLRHashString("upper", this), new Integer(57));

	literals.put(new ANTLRHashString("abs", this), new Integer(37));

	literals.put(new ANTLRHashString("floor", this), new Integer(39));

	literals.put(new ANTLRHashString("chr", this), new Integer(46));

	literals.put(new ANTLRHashString("connect", this), new Integer(108));

	literals.put(new ANTLRHashString("convert", this), new Integer(70));

	literals.put(new ANTLRHashString("and", this), new Integer(90));

	literals.put(new ANTLRHashString("instr", this), new Integer(59));

	literals.put(new ANTLRHashString("concat", this), new Integer(61));

	literals.put(new ANTLRHashString("asc", this), new Integer(115));

	literals.put(new ANTLRHashString("desc", this), new Integer(116));

	literals.put(new ANTLRHashString("select", this), new Integer(19));

	literals.put(new ANTLRHashString("intersect", this), new Integer(112));

	literals.put(new ANTLRHashString("decode", this), new Integer(77));

	literals.put(new ANTLRHashString("ceil", this), new Integer(38));

	literals.put(new ANTLRHashString("rpad", this), new Integer(52));

	literals.put(new ANTLRHashString("exists", this), new Integer(99));

	literals.put(new ANTLRHashString("distinct", this), new Integer(21));

	literals.put(new ANTLRHashString("nowait", this), new Integer(120));

	literals.put(new ANTLRHashString("group", this), new Integer(110));

	literals.put(new ANTLRHashString("sysdate", this), new Integer(86));

	literals.put(new ANTLRHashString("where", this), new Integer(23));

	literals.put(new ANTLRHashString("set", this), new Integer(122));

	literals.put(new ANTLRHashString("user", this), new Integer(85));

	literals.put(new ANTLRHashString("to_char", this), new Integer(74));

	literals.put(new ANTLRHashString("ascii", this), new Integer(58));

	literals.put(new ANTLRHashString("lpad", this), new Integer(49));

	literals.put(new ANTLRHashString("rawtohex", this), new Integer(72));

	literals.put(new ANTLRHashString("prior", this), new Integer(91));

	literals.put(new ANTLRHashString("minus", this), new Integer(113));

	literals.put(new ANTLRHashString("?", this), new Integer(87));

	literals.put(new ANTLRHashString("avg", this), new Integer(62));

	literals.put(new ANTLRHashString("order", this), new Integer(114));

	literals.put(new ANTLRHashString("mod", this), new Integer(40));

	literals.put(new ANTLRHashString("variance", this), new Integer(68));

	literals.put(new ANTLRHashString("in", this), new Integer(93));

	literals.put(new ANTLRHashString("null", this), new Integer(34));

	literals.put(new ANTLRHashString("to_date", this), new Integer(75));

	literals.put(new ANTLRHashString("escape", this), new Integer(95));

	literals.put(new ANTLRHashString("length", this), new Integer(60));

	literals.put(new ANTLRHashString("having", this), new Integer(111));

	literals.put(new ANTLRHashString("of", this), new Integer(119));

	literals.put(new ANTLRHashString("least", this), new Integer(80));

	literals.put(new ANTLRHashString("rtrim", this), new Integer(53));

	literals.put(new ANTLRHashString("union", this), new Integer(16));

	literals.put(new ANTLRHashString("between", this), new Integer(96));

	literals.put(new ANTLRHashString("or", this), new Integer(89));

	literals.put(new ANTLRHashString("stddev", this), new Integer(66));

	literals.put(new ANTLRHashString("ltrim", this), new Integer(50));

	literals.put(new ANTLRHashString("max", this), new Integer(64));

	literals.put(new ANTLRHashString("from", this), new Integer(22));

	literals.put(new ANTLRHashString("is", this), new Integer(97));

	literals.put(new ANTLRHashString("power", this), new Integer(41));

	literals.put(new ANTLRHashString("greatest", this), new Integer(79));

	literals.put(new ANTLRHashString("dump", this), new Integer(78));

	literals.put(new ANTLRHashString("translate", this), new Integer(56));

	literals.put(new ANTLRHashString("like", this), new Integer(94));

	literals.put(new ANTLRHashString("delete", this), new Integer(121));

	literals.put(new ANTLRHashString("substr", this), new Integer(55));

	literals.put(new ANTLRHashString("any", this), new Integer(98));

	literals.put(new ANTLRHashString("trunc", this), new Integer(45));

	literals.put(new ANTLRHashString("update", this), new Integer(118));

	literals.put(new ANTLRHashString("to_number", this), new Integer(76));

	literals.put(new ANTLRHashString("rowidtochar", this), new Integer(73));

	literals.put(new ANTLRHashString("uid", this), new Integer(82));

	literals.put(new ANTLRHashString("with", this), new Integer(107));

	literals.put(new ANTLRHashString("start", this), new Integer(106));

	literals.put(new ANTLRHashString("not", this), new Integer(92));

	literals.put(new ANTLRHashString("by", this), new Integer(109));

	literals.put(new ANTLRHashString("as", this), new Integer(29));

}



public Token nextToken() throws TokenStreamException {

	Token theRetToken=null;

tryAgain:

	for (;;) {

		Token _token = null;

		int _ttype = Token.INVALID_TYPE;

		resetText();

		try {   // for char stream error handling

			try {   // for lexical error handling

				switch ( LA(1)) {

				case '?':

				{

					mQUESTION_MARK(true);

					theRetToken=_returnToken;

					break;

				}

				case '\'':

				{

					mQUOTED_STRING(true);

					theRetToken=_returnToken;

					break;

				}

				case ';':

				{

					mSEMI(true);

					theRetToken=_returnToken;

					break;

				}

				case ',':

				{

					mCOMMA(true);

					theRetToken=_returnToken;

					break;

				}

				case '*':

				{

					mASTERISK(true);

					theRetToken=_returnToken;

					break;

				}

				case '@':

				{

					mAT_SIGN(true);

					theRetToken=_returnToken;

					break;

				}

				case '(':

				{

					mOPEN_PAREN(true);

					theRetToken=_returnToken;

					break;

				}

				case ')':

				{

					mCLOSE_PAREN(true);

					theRetToken=_returnToken;

					break;

				}

				case '|':

				{

					mVERTBAR(true);

					theRetToken=_returnToken;

					break;

				}

				case '=':

				{

					mEQ(true);

					theRetToken=_returnToken;

					break;

				}

				case '!':  case '<':  case '^':

				{

					mNOT_EQ(true);

					theRetToken=_returnToken;

					break;

				}

				case '>':

				{

					mGT(true);

					theRetToken=_returnToken;

					break;

				}

				case ':':

				{

					mBIND(true);

					theRetToken=_returnToken;

					break;

				}

				case '"':

				{

					mDOUBLE_QUOTE(true);

					theRetToken=_returnToken;

					break;

				}

				case '\t':  case '\n':  case '\r':  case ' ':

				{

					mWS(true);

					theRetToken=_returnToken;

					break;

				}

				default:

					if ((LA(1)=='/') && (LA(2)=='*')) {

						mML_COMMENT(true);

						theRetToken=_returnToken;

					}

					else if (((LA(1) >= 'a' && LA(1) <= 'z')) && (true)) {

						mIDENTIFIER(true);

						theRetToken=_returnToken;

					}

					else if ((LA(1)=='.') && (true)) {

						mDOT(true);

						theRetToken=_returnToken;

					}

					else if ((LA(1)=='+') && (true)) {

						mPLUS(true);

						theRetToken=_returnToken;

					}

					else if ((LA(1)=='-') && (true)) {

						mMINUS(true);

						theRetToken=_returnToken;

					}

					else if ((LA(1)=='/') && (true)) {

						mDIVIDE(true);

						theRetToken=_returnToken;

					}

					else if ((_tokenSet_0.member(LA(1))) && (true)) {

						mNUMBER(true);

						theRetToken=_returnToken;

					}

					else if ((_tokenSet_1.member(LA(1))) && (true)) {

						mBIND_NAME(true);

						theRetToken=_returnToken;

					}

				else {

					if (LA(1)==EOF_CHAR) {uponEOF(); _returnToken = makeToken(Token.EOF_TYPE);}

				else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}

				}

				}

				if ( _returnToken==null ) continue tryAgain; // found SKIP token

				_ttype = _returnToken.getType();

				_returnToken.setType(_ttype);

				return _returnToken;

			}

			catch (RecognitionException e) {

				throw new TokenStreamRecognitionException(e);

			}

		}

		catch (CharStreamException cse) {

			if ( cse instanceof CharStreamIOException ) {

				throw new TokenStreamIOException(((CharStreamIOException)cse).io);

			}

			else {

				throw new TokenStreamException(cse.getMessage());

			}

		}

	}

}



	public final void mIDENTIFIER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = IDENTIFIER;

		int _saveIndex;

		

		matchRange('a','z');

		{

		_loop232:

		do {

			switch ( LA(1)) {

			case 'a':  case 'b':  case 'c':  case 'd':

			case 'e':  case 'f':  case 'g':  case 'h':

			case 'i':  case 'j':  case 'k':  case 'l':

			case 'm':  case 'n':  case 'o':  case 'p':

			case 'q':  case 'r':  case 's':  case 't':

			case 'u':  case 'v':  case 'w':  case 'x':

			case 'y':  case 'z':

			{

				matchRange('a','z');

				break;

			}

			case '0':  case '1':  case '2':  case '3':

			case '4':  case '5':  case '6':  case '7':

			case '8':  case '9':

			{

				matchRange('0','9');

				break;

			}

			case '_':

			{

				match('_');

				break;

			}

			case '$':

			{

				match('$');

				break;

			}

			case '#':

			{

				match('#');

				break;

			}

			default:

			{

				break _loop232;

			}

			}

		} while (true);

		}

		_ttype = testLiteralsTable(_ttype);

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mQUESTION_MARK(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = QUESTION_MARK;

		int _saveIndex;

		

		match('?');

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mQUOTED_STRING(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = QUOTED_STRING;

		int _saveIndex;

		

		match('\'');

		{

		_loop236:

		do {

			if ((_tokenSet_2.member(LA(1)))) {

				matchNot('\'');

			}

			else {

				break _loop236;

			}

			

		} while (true);

		}

		match('\'');

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mSEMI(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = SEMI;

		int _saveIndex;

		

		match(';');

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mDOT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = DOT;

		int _saveIndex;

		

		match('.');

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mCOMMA(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = COMMA;

		int _saveIndex;

		

		match(',');

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mASTERISK(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = ASTERISK;

		int _saveIndex;

		

		match('*');

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mAT_SIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = AT_SIGN;

		int _saveIndex;

		

		match('@');

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mOPEN_PAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = OPEN_PAREN;

		int _saveIndex;

		

		match('(');

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mCLOSE_PAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = CLOSE_PAREN;

		int _saveIndex;

		

		match(')');

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mPLUS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = PLUS;

		int _saveIndex;

		

		match('+');

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mMINUS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = MINUS;

		int _saveIndex;

		

		match('-');

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mDIVIDE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = DIVIDE;

		int _saveIndex;

		

		match('/');

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mVERTBAR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = VERTBAR;

		int _saveIndex;

		

		match('|');

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mEQ(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = EQ;

		int _saveIndex;

		

		match('=');

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mNOT_EQ(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = NOT_EQ;

		int _saveIndex;

		

		switch ( LA(1)) {

		case '<':

		{

			match('<');

			if ( inputState.guessing==0 ) {

				_ttype = LT;

			}

			{

			switch ( LA(1)) {

			case '>':

			{

				{

				match('>');

				if ( inputState.guessing==0 ) {

					_ttype = NOT_EQ;

				}

				}

				break;

			}

			case '=':

			{

				{

				match('=');

				if ( inputState.guessing==0 ) {

					_ttype = LE;

				}

				}

				break;

			}

			default:

				{

				}

			}

			}

			break;

		}

		case '!':

		{

			match("!=");

			break;

		}

		case '^':

		{

			match("^=");

			break;

		}

		default:

		{

			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());

		}

		}

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mGT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = GT;

		int _saveIndex;

		

		match('>');

		{

		if ((LA(1)=='=')) {

			match('=');

			if ( inputState.guessing==0 ) {

				_ttype = GE;

			}

		}

		else {

		}

		

		}

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mNUMBER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = NUMBER;

		int _saveIndex;

		

		{

		switch ( LA(1)) {

		case '+':

		{

			mPLUS(false);

			break;

		}

		case '-':

		{

			mMINUS(false);

			break;

		}

		case '.':  case '0':  case '1':  case '2':

		case '3':  case '4':  case '5':  case '6':

		case '7':  case '8':  case '9':

		{

			break;

		}

		default:

		{

			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());

		}

		}

		}

		{

		boolean synPredMatched259 = false;

		if ((((LA(1) >= '0' && LA(1) <= '9')) && (_tokenSet_3.member(LA(2))))) {

			int _m259 = mark();

			synPredMatched259 = true;

			inputState.guessing++;

			try {

				{

				mN(false);

				mDOT(false);

				mN(false);

				}

			}

			catch (RecognitionException pe) {

				synPredMatched259 = false;

			}

			rewind(_m259);

			inputState.guessing--;

		}

		if ( synPredMatched259 ) {

			mN(false);

			mDOT(false);

			mN(false);

		}

		else if ((LA(1)=='.')) {

			mDOT(false);

			mN(false);

		}

		else if (((LA(1) >= '0' && LA(1) <= '9')) && (true)) {

			mN(false);

		}

		else {

			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());

		}

		

		}

		{

		if ((LA(1)=='e')) {

			match("e");

			{

			switch ( LA(1)) {

			case '+':

			{

				mPLUS(false);

				break;

			}

			case '-':

			{

				mMINUS(false);

				break;

			}

			case '0':  case '1':  case '2':  case '3':

			case '4':  case '5':  case '6':  case '7':

			case '8':  case '9':

			{

				break;

			}

			default:

			{

				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());

			}

			}

			}

			mN(false);

		}

		else {

		}

		

		}

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	protected final void mN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = N;

		int _saveIndex;

		

		matchRange('0','9');

		{

		_loop264:

		do {

			if (((LA(1) >= '0' && LA(1) <= '9'))) {

				matchRange('0','9');

			}

			else {

				break _loop264;

			}

			

		} while (true);

		}

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mBIND_NAME(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = BIND_NAME;

		int _saveIndex;

		

		switch ( LA(1)) {

		case 'a':  case 'b':  case 'c':  case 'd':

		case 'e':  case 'f':  case 'g':  case 'h':

		case 'i':  case 'j':  case 'k':  case 'l':

		case 'm':  case 'n':  case 'o':  case 'p':

		case 'q':  case 'r':  case 's':  case 't':

		case 'u':  case 'v':  case 'w':  case 'x':

		case 'y':  case 'z':

		{

			matchRange('a','z');

			break;

		}

		case '0':  case '1':  case '2':  case '3':

		case '4':  case '5':  case '6':  case '7':

		case '8':  case '9':

		{

			matchRange('0','9');

			break;

		}

		case '_':

		{

			match('_');

			{

			_loop267:

			do {

				switch ( LA(1)) {

				case 'a':  case 'b':  case 'c':  case 'd':

				case 'e':  case 'f':  case 'g':  case 'h':

				case 'i':  case 'j':  case 'k':  case 'l':

				case 'm':  case 'n':  case 'o':  case 'p':

				case 'q':  case 'r':  case 's':  case 't':

				case 'u':  case 'v':  case 'w':  case 'x':

				case 'y':  case 'z':

				{

					matchRange('a','z');

					break;

				}

				case '0':  case '1':  case '2':  case '3':

				case '4':  case '5':  case '6':  case '7':

				case '8':  case '9':

				{

					matchRange('0','9');

					break;

				}

				case '_':

				{

					match('_');

					break;

				}

				case '$':

				{

					match('$');

					break;

				}

				case '#':

				{

					match('#');

					break;

				}

				default:

				{

					break _loop267;

				}

				}

			} while (true);

			}

			break;

		}

		default:

		{

			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());

		}

		}

		_ttype = testLiteralsTable(_ttype);

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mBIND(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = BIND;

		int _saveIndex;

		

		match(':');

		mBIND_NAME(false);

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mDOUBLE_QUOTE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = DOUBLE_QUOTE;

		int _saveIndex;

		

		match('"');

		if ( inputState.guessing==0 ) {

			_ttype = Token.SKIP;

		}

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mWS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = WS;

		int _saveIndex;

		

		{

		switch ( LA(1)) {

		case ' ':

		{

			match(' ');

			break;

		}

		case '\t':

		{

			match('\t');

			break;

		}

		case '\n':

		{

			match('\n');

			if ( inputState.guessing==0 ) {

				newline();

			}

			break;

		}

		default:

			if ((LA(1)=='\r') && (LA(2)=='\n')) {

				match('\r');

				match('\n');

				if ( inputState.guessing==0 ) {

					newline();

				}

			}

			else if ((LA(1)=='\r') && (true)) {

				match('\r');

				if ( inputState.guessing==0 ) {

					newline();

				}

			}

		else {

			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());

		}

		}

		}

		if ( inputState.guessing==0 ) {

			_ttype = Token.SKIP;

		}

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	public final void mML_COMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {

		int _ttype; Token _token=null; int _begin=text.length();

		_ttype = ML_COMMENT;

		int _saveIndex;

		

		match("/*");

		{

		_loop275:

		do {

			switch ( LA(1)) {

			case '\n':

			{

				match('\n');

				if ( inputState.guessing==0 ) {

					newline();

				}

				break;

			}

			case '\u0003':  case '\u0004':  case '\u0005':  case '\u0006':

			case '\u0007':  case '\u0008':  case '\t':  case '\u000b':

			case '\u000c':  case '\u000e':  case '\u000f':  case '\u0010':

			case '\u0011':  case '\u0012':  case '\u0013':  case '\u0014':

			case '\u0015':  case '\u0016':  case '\u0017':  case '\u0018':

			case '\u0019':  case '\u001a':  case '\u001b':  case '\u001c':

			case '\u001d':  case '\u001e':  case '\u001f':  case ' ':

			case '!':  case '"':  case '#':  case '$':

			case '%':  case '&':  case '\'':  case '(':

			case ')':  case '+':  case ',':  case '-':

			case '.':  case '/':  case '0':  case '1':

			case '2':  case '3':  case '4':  case '5':

			case '6':  case '7':  case '8':  case '9':

			case ':':  case ';':  case '<':  case '=':

			case '>':  case '?':  case '@':  case 'A':

			case 'B':  case 'C':  case 'D':  case 'E':

			case 'F':  case 'G':  case 'H':  case 'I':

			case 'J':  case 'K':  case 'L':  case 'M':

			case 'N':  case 'O':  case 'P':  case 'Q':

			case 'R':  case 'S':  case 'T':  case 'U':

			case 'V':  case 'W':  case 'X':  case 'Y':

			case 'Z':  case '[':  case '\\':  case ']':

			case '^':  case '_':  case '`':  case 'a':

			case 'b':  case 'c':  case 'd':  case 'e':

			case 'f':  case 'g':  case 'h':  case 'i':

			case 'j':  case 'k':  case 'l':  case 'm':

			case 'n':  case 'o':  case 'p':  case 'q':

			case 'r':  case 's':  case 't':  case 'u':

			case 'v':  case 'w':  case 'x':  case 'y':

			case 'z':  case '{':  case '|':  case '}':

			case '~':  case '\u007f':

			{

				{

				match(_tokenSet_4);

				}

				break;

			}

			default:

				if (((LA(1)=='*') && ((LA(2) >= '\u0003' && LA(2) <= '\u007f')))&&( LA(2)!='/' )) {

					match('*');

				}

				else if ((LA(1)=='\r') && (LA(2)=='\n')) {

					match('\r');

					match('\n');

					if ( inputState.guessing==0 ) {

						newline();

					}

				}

				else if ((LA(1)=='\r') && ((LA(2) >= '\u0003' && LA(2) <= '\u007f'))) {

					match('\r');

					if ( inputState.guessing==0 ) {

						newline();

					}

				}

			else {

				break _loop275;

			}

			}

		} while (true);

		}

		match("*/");

		if ( inputState.guessing==0 ) {

			_ttype = Token.SKIP;

		}

		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {

			_token = makeToken(_ttype);

			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));

		}

		_returnToken = _token;

	}

	

	

	private static final long[] mk_tokenSet_0() {

		long[] data = { 288063250384289792L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

	private static final long[] mk_tokenSet_1() {

		long[] data = { 287948901175001088L, 576460745860972544L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());

	private static final long[] mk_tokenSet_2() {

		long[] data = { -549755813896L, -1L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());

	private static final long[] mk_tokenSet_3() {

		long[] data = { 288019269919178752L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());

	private static final long[] mk_tokenSet_4() {

		long[] data = { -4398046520328L, -1L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());

	

	}

