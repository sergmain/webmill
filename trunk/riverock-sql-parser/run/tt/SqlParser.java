// $ANTLR 2.7.3: "SqlSQL2.g" -> "SqlParser.java"$

package tt;



import antlr.TokenBuffer;

import antlr.TokenStreamException;

import antlr.TokenStreamIOException;

import antlr.ANTLRException;

import antlr.LLkParser;

import antlr.Token;

import antlr.TokenStream;

import antlr.RecognitionException;

import antlr.NoViableAltException;

import antlr.MismatchedTokenException;

import antlr.SemanticException;

import antlr.ParserSharedInputState;

import antlr.collections.impl.BitSet;

import antlr.collections.AST;

import java.util.Hashtable;

import antlr.ASTFactory;

import antlr.ASTPair;

import antlr.collections.impl.ASTArray;



public class SqlParser extends antlr.LLkParser       implements SqlTokenTypes

 {



protected SqlParser(TokenBuffer tokenBuf, int k) {

  super(tokenBuf,k);

  tokenNames = _tokenNames;

  buildTokenTypeASTClassMap();

  astFactory = new ASTFactory(getTokenTypeToASTClassMap());

}



public SqlParser(TokenBuffer tokenBuf) {

  this(tokenBuf,4);

}



protected SqlParser(TokenStream lexer, int k) {

  super(lexer,k);

  tokenNames = _tokenNames;

  buildTokenTypeASTClassMap();

  astFactory = new ASTFactory(getTokenTypeToASTClassMap());

}



public SqlParser(TokenStream lexer) {

  this(lexer,4);

}



public SqlParser(ParserSharedInputState state) {

  super(state,4);

  tokenNames = _tokenNames;

  buildTokenTypeASTClassMap();

  astFactory = new ASTFactory(getTokenTypeToASTClassMap());

}



	public final void start_rule() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST start_rule_AST = null;

		

		try {      // for error handling

			{

			_loop3:

			do {

				if ((_tokenSet_0.member(LA(1)))) {

					sql_statement();

					astFactory.addASTChild(currentAST, returnAST);

				}

				else {

					break _loop3;

				}

				

			} while (true);

			}

			AST tmp1_AST = null;

			tmp1_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp1_AST);

			match(Token.EOF_TYPE);

			start_rule_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_1);

			} else {

			  throw ex;

			}

		}

		returnAST = start_rule_AST;

	}

	

	public final void sql_statement() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST sql_statement_AST = null;

		

		try {      // for error handling

			sql_command();

			astFactory.addASTChild(currentAST, returnAST);

			{

			switch ( LA(1)) {

			case SEMI:

			{

				AST tmp2_AST = null;

				tmp2_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp2_AST);

				match(SEMI);

				break;

			}

			case EOF:

			case OPEN_PAREN:

			case LITERAL_select:

			case LITERAL_update:

			case LITERAL_delete:

			{

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			if ( inputState.guessing==0 ) {

				sql_statement_AST = (AST)currentAST.root;

				sql_statement_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(SQL_STATEMENT,"sql_statement")).add(sql_statement_AST));

				currentAST.root = sql_statement_AST;

				currentAST.child = sql_statement_AST!=null &&sql_statement_AST.getFirstChild()!=null ?

					sql_statement_AST.getFirstChild() : sql_statement_AST;

				currentAST.advanceChildToEnd();

			}

			sql_statement_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_2);

			} else {

			  throw ex;

			}

		}

		returnAST = sql_statement_AST;

	}

	

	public final void sql_command() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST sql_command_AST = null;

		

		try {      // for error handling

			to_modify_data();

			astFactory.addASTChild(currentAST, returnAST);

			sql_command_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_3);

			} else {

			  throw ex;

			}

		}

		returnAST = sql_command_AST;

	}

	

	public final void to_modify_data() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST to_modify_data_AST = null;

		

		try {      // for error handling

			switch ( LA(1)) {

			case OPEN_PAREN:

			case LITERAL_select:

			{

				select_command();

				astFactory.addASTChild(currentAST, returnAST);

				to_modify_data_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_update:

			{

				update_command();

				astFactory.addASTChild(currentAST, returnAST);

				to_modify_data_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_delete:

			{

				delete_command();

				astFactory.addASTChild(currentAST, returnAST);

				to_modify_data_AST = (AST)currentAST.root;

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_3);

			} else {

			  throw ex;

			}

		}

		returnAST = to_modify_data_AST;

	}

	

	public final void select_command() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST select_command_AST = null;

		

		try {      // for error handling

			select_statement();

			astFactory.addASTChild(currentAST, returnAST);

			{

			_loop10:

			do {

				if ((LA(1)==LITERAL_union) && (LA(2)==OPEN_PAREN||LA(2)==LITERAL_select) && (_tokenSet_4.member(LA(3))) && (_tokenSet_5.member(LA(4)))) {

					AST tmp3_AST = null;

					tmp3_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp3_AST);

					match(LITERAL_union);

					select_statement();

					astFactory.addASTChild(currentAST, returnAST);

				}

				else {

					break _loop10;

				}

				

			} while (true);

			}

			select_command_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_6);

			} else {

			  throw ex;

			}

		}

		returnAST = select_command_AST;

	}

	

	public final void update_command() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST update_command_AST = null;

		

		try {      // for error handling

			boolean synPredMatched212 = false;

			if (((LA(1)==LITERAL_update) && (_tokenSet_7.member(LA(2))) && (_tokenSet_8.member(LA(3))) && (_tokenSet_9.member(LA(4))))) {

				int _m212 = mark();

				synPredMatched212 = true;

				inputState.guessing++;

				try {

					{

					subquery_update();

					}

				}

				catch (RecognitionException pe) {

					synPredMatched212 = false;

				}

				rewind(_m212);

				inputState.guessing--;

			}

			if ( synPredMatched212 ) {

				subquery_update();

				astFactory.addASTChild(currentAST, returnAST);

				update_command_AST = (AST)currentAST.root;

			}

			else if ((LA(1)==LITERAL_update) && (_tokenSet_7.member(LA(2))) && (_tokenSet_8.member(LA(3))) && (_tokenSet_10.member(LA(4)))) {

				simple_update();

				astFactory.addASTChild(currentAST, returnAST);

				update_command_AST = (AST)currentAST.root;

			}

			else {

				throw new NoViableAltException(LT(1), getFilename());

			}

			

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_3);

			} else {

			  throw ex;

			}

		}

		returnAST = update_command_AST;

	}

	

	public final void delete_command() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST delete_command_AST = null;

		

		try {      // for error handling

			AST tmp4_AST = null;

			tmp4_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp4_AST);

			match(LITERAL_delete);

			{

			switch ( LA(1)) {

			case LITERAL_from:

			{

				AST tmp5_AST = null;

				tmp5_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp5_AST);

				match(LITERAL_from);

				break;

			}

			case QUOTED_STRING:

			case LITERAL_abs:

			case LITERAL_ceil:

			case LITERAL_floor:

			case LITERAL_power:

			case LITERAL_round:

			case LITERAL_sign:

			case LITERAL_sqrt:

			case LITERAL_trunc:

			case LITERAL_chr:

			case LITERAL_initcap:

			case LITERAL_lower:

			case LITERAL_lpad:

			case LITERAL_ltrim:

			case LITERAL_replace:

			case LITERAL_rpad:

			case LITERAL_rtrim:

			case LITERAL_soundex:

			case LITERAL_substr:

			case LITERAL_translate:

			case LITERAL_upper:

			case LITERAL_ascii:

			case LITERAL_instr:

			case LITERAL_length:

			case LITERAL_concat:

			case LITERAL_count:

			case LITERAL_chartorowid:

			case LITERAL_convert:

			case LITERAL_hextoraw:

			case LITERAL_rawtohex:

			case LITERAL_rowidtochar:

			case LITERAL_to_char:

			case LITERAL_to_date:

			case LITERAL_to_number:

			case LITERAL_decode:

			case LITERAL_dump:

			case LITERAL_greatest:

			case LITERAL_least:

			case LITERAL_nvl:

			case LITERAL_userenv:

			case LITERAL_vsize:

			case LITERAL_user:

			case LITERAL_sysdate:

			case LITERAL_intersect:

			case IDENTIFIER:

			{

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			table_alias();

			astFactory.addASTChild(currentAST, returnAST);

			{

			switch ( LA(1)) {

			case LITERAL_where:

			{

				AST tmp6_AST = null;

				tmp6_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp6_AST);

				match(LITERAL_where);

				condition();

				astFactory.addASTChild(currentAST, returnAST);

				break;

			}

			case EOF:

			case SEMI:

			case OPEN_PAREN:

			case LITERAL_select:

			case LITERAL_update:

			case LITERAL_delete:

			{

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			delete_command_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_3);

			} else {

			  throw ex;

			}

		}

		returnAST = delete_command_AST;

	}

	

	public final void select_statement() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST select_statement_AST = null;

		

		try {      // for error handling

			switch ( LA(1)) {

			case OPEN_PAREN:

			{

				AST tmp7_AST = null;

				tmp7_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp7_AST);

				match(OPEN_PAREN);

				select_command();

				astFactory.addASTChild(currentAST, returnAST);

				AST tmp8_AST = null;

				tmp8_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp8_AST);

				match(CLOSE_PAREN);

				select_statement_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_select:

			{

				select_expression();

				astFactory.addASTChild(currentAST, returnAST);

				select_statement_AST = (AST)currentAST.root;

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_6);

			} else {

			  throw ex;

			}

		}

		returnAST = select_statement_AST;

	}

	

	public final void select_expression() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST select_expression_AST = null;

		

		try {      // for error handling

			AST tmp9_AST = null;

			tmp9_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp9_AST);

			match(LITERAL_select);

			{

			switch ( LA(1)) {

			case LITERAL_all:

			{

				AST tmp10_AST = null;

				tmp10_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp10_AST);

				match(LITERAL_all);

				break;

			}

			case LITERAL_distinct:

			{

				AST tmp11_AST = null;

				tmp11_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp11_AST);

				match(LITERAL_distinct);

				break;

			}

			case OPEN_PAREN:

			case ASTERISK:

			case PLUS:

			case MINUS:

			case NUMBER:

			case QUOTED_STRING:

			case LITERAL_null:

			case QUESTION_MARK:

			case BIND_NAME:

			case LITERAL_abs:

			case LITERAL_ceil:

			case LITERAL_floor:

			case LITERAL_mod:

			case LITERAL_power:

			case LITERAL_round:

			case LITERAL_sign:

			case LITERAL_sqrt:

			case LITERAL_trunc:

			case LITERAL_chr:

			case LITERAL_initcap:

			case LITERAL_lower:

			case LITERAL_lpad:

			case LITERAL_ltrim:

			case LITERAL_replace:

			case LITERAL_rpad:

			case LITERAL_rtrim:

			case LITERAL_soundex:

			case LITERAL_substr:

			case LITERAL_translate:

			case LITERAL_upper:

			case LITERAL_ascii:

			case LITERAL_instr:

			case LITERAL_length:

			case LITERAL_concat:

			case LITERAL_avg:

			case LITERAL_count:

			case LITERAL_max:

			case LITERAL_min:

			case LITERAL_stddev:

			case LITERAL_sum:

			case LITERAL_variance:

			case LITERAL_chartorowid:

			case LITERAL_convert:

			case LITERAL_hextoraw:

			case LITERAL_rawtohex:

			case LITERAL_rowidtochar:

			case LITERAL_to_char:

			case LITERAL_to_date:

			case LITERAL_to_number:

			case LITERAL_decode:

			case LITERAL_dump:

			case LITERAL_greatest:

			case LITERAL_least:

			case LITERAL_nvl:

			case LITERAL_uid:

			case LITERAL_userenv:

			case LITERAL_vsize:

			case LITERAL_user:

			case LITERAL_sysdate:

			case LITERAL_intersect:

			case IDENTIFIER:

			{

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			select_list();

			astFactory.addASTChild(currentAST, returnAST);

			AST tmp12_AST = null;

			tmp12_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp12_AST);

			match(LITERAL_from);

			table_reference_list();

			astFactory.addASTChild(currentAST, returnAST);

			{

			switch ( LA(1)) {

			case LITERAL_where:

			{

				AST tmp13_AST = null;

				tmp13_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp13_AST);

				match(LITERAL_where);

				where_condition();

				astFactory.addASTChild(currentAST, returnAST);

				break;

			}

			case EOF:

			case SEMI:

			case LITERAL_union:

			case OPEN_PAREN:

			case CLOSE_PAREN:

			case LITERAL_select:

			case LITERAL_start:

			case LITERAL_connect:

			case LITERAL_group:

			case LITERAL_intersect:

			case LITERAL_minus:

			case LITERAL_order:

			case LITERAL_for:

			case LITERAL_update:

			case LITERAL_delete:

			{

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			{

			switch ( LA(1)) {

			case LITERAL_start:

			case LITERAL_connect:

			{

				connect_clause();

				astFactory.addASTChild(currentAST, returnAST);

				break;

			}

			case EOF:

			case SEMI:

			case LITERAL_union:

			case OPEN_PAREN:

			case CLOSE_PAREN:

			case LITERAL_select:

			case LITERAL_group:

			case LITERAL_intersect:

			case LITERAL_minus:

			case LITERAL_order:

			case LITERAL_for:

			case LITERAL_update:

			case LITERAL_delete:

			{

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			{

			switch ( LA(1)) {

			case LITERAL_group:

			{

				group_clause();

				astFactory.addASTChild(currentAST, returnAST);

				break;

			}

			case EOF:

			case SEMI:

			case LITERAL_union:

			case OPEN_PAREN:

			case CLOSE_PAREN:

			case LITERAL_select:

			case LITERAL_intersect:

			case LITERAL_minus:

			case LITERAL_order:

			case LITERAL_for:

			case LITERAL_update:

			case LITERAL_delete:

			{

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			{

			boolean synPredMatched21 = false;

			if (((LA(1)==LITERAL_union||LA(1)==LITERAL_intersect||LA(1)==LITERAL_minus) && (LA(2)==OPEN_PAREN||LA(2)==LITERAL_select||LA(2)==LITERAL_all) && (_tokenSet_4.member(LA(3))) && (_tokenSet_5.member(LA(4))))) {

				int _m21 = mark();

				synPredMatched21 = true;

				inputState.guessing++;

				try {

					{

					set_clause();

					}

				}

				catch (RecognitionException pe) {

					synPredMatched21 = false;

				}

				rewind(_m21);

				inputState.guessing--;

			}

			if ( synPredMatched21 ) {

				set_clause();

				astFactory.addASTChild(currentAST, returnAST);

			}

			else if ((_tokenSet_6.member(LA(1))) && (_tokenSet_11.member(LA(2))) && (_tokenSet_12.member(LA(3))) && (_tokenSet_13.member(LA(4)))) {

			}

			else {

				throw new NoViableAltException(LT(1), getFilename());

			}

			

			}

			{

			boolean synPredMatched24 = false;

			if (((LA(1)==LITERAL_order) && (LA(2)==LITERAL_by) && (_tokenSet_14.member(LA(3))) && (_tokenSet_15.member(LA(4))))) {

				int _m24 = mark();

				synPredMatched24 = true;

				inputState.guessing++;

				try {

					{

					order_clause();

					}

				}

				catch (RecognitionException pe) {

					synPredMatched24 = false;

				}

				rewind(_m24);

				inputState.guessing--;

			}

			if ( synPredMatched24 ) {

				order_clause();

				astFactory.addASTChild(currentAST, returnAST);

			}

			else if ((_tokenSet_6.member(LA(1))) && (_tokenSet_11.member(LA(2))) && (_tokenSet_12.member(LA(3))) && (_tokenSet_13.member(LA(4)))) {

			}

			else {

				throw new NoViableAltException(LT(1), getFilename());

			}

			

			}

			{

			boolean synPredMatched27 = false;

			if (((LA(1)==LITERAL_for) && (LA(2)==LITERAL_update) && (_tokenSet_16.member(LA(3))) && (_tokenSet_11.member(LA(4))))) {

				int _m27 = mark();

				synPredMatched27 = true;

				inputState.guessing++;

				try {

					{

					update_clause();

					}

				}

				catch (RecognitionException pe) {

					synPredMatched27 = false;

				}

				rewind(_m27);

				inputState.guessing--;

			}

			if ( synPredMatched27 ) {

				update_clause();

				astFactory.addASTChild(currentAST, returnAST);

			}

			else if ((_tokenSet_6.member(LA(1))) && (_tokenSet_11.member(LA(2))) && (_tokenSet_12.member(LA(3))) && (_tokenSet_13.member(LA(4)))) {

			}

			else {

				throw new NoViableAltException(LT(1), getFilename());

			}

			

			}

			select_expression_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_6);

			} else {

			  throw ex;

			}

		}

		returnAST = select_expression_AST;

	}

	

	public final void select_list() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST select_list_AST = null;

		

		try {      // for error handling

			{

			switch ( LA(1)) {

			case OPEN_PAREN:

			case PLUS:

			case MINUS:

			case NUMBER:

			case QUOTED_STRING:

			case LITERAL_null:

			case QUESTION_MARK:

			case BIND_NAME:

			case LITERAL_abs:

			case LITERAL_ceil:

			case LITERAL_floor:

			case LITERAL_mod:

			case LITERAL_power:

			case LITERAL_round:

			case LITERAL_sign:

			case LITERAL_sqrt:

			case LITERAL_trunc:

			case LITERAL_chr:

			case LITERAL_initcap:

			case LITERAL_lower:

			case LITERAL_lpad:

			case LITERAL_ltrim:

			case LITERAL_replace:

			case LITERAL_rpad:

			case LITERAL_rtrim:

			case LITERAL_soundex:

			case LITERAL_substr:

			case LITERAL_translate:

			case LITERAL_upper:

			case LITERAL_ascii:

			case LITERAL_instr:

			case LITERAL_length:

			case LITERAL_concat:

			case LITERAL_avg:

			case LITERAL_count:

			case LITERAL_max:

			case LITERAL_min:

			case LITERAL_stddev:

			case LITERAL_sum:

			case LITERAL_variance:

			case LITERAL_chartorowid:

			case LITERAL_convert:

			case LITERAL_hextoraw:

			case LITERAL_rawtohex:

			case LITERAL_rowidtochar:

			case LITERAL_to_char:

			case LITERAL_to_date:

			case LITERAL_to_number:

			case LITERAL_decode:

			case LITERAL_dump:

			case LITERAL_greatest:

			case LITERAL_least:

			case LITERAL_nvl:

			case LITERAL_uid:

			case LITERAL_userenv:

			case LITERAL_vsize:

			case LITERAL_user:

			case LITERAL_sysdate:

			case LITERAL_intersect:

			case IDENTIFIER:

			{

				displayed_column();

				astFactory.addASTChild(currentAST, returnAST);

				{

				_loop33:

				do {

					if ((LA(1)==COMMA)) {

						AST tmp14_AST = null;

						tmp14_AST = astFactory.create(LT(1));

						astFactory.addASTChild(currentAST, tmp14_AST);

						match(COMMA);

						displayed_column();

						astFactory.addASTChild(currentAST, returnAST);

					}

					else {

						break _loop33;

					}

					

				} while (true);

				}

				break;

			}

			case ASTERISK:

			{

				AST tmp15_AST = null;

				tmp15_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp15_AST);

				match(ASTERISK);

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			if ( inputState.guessing==0 ) {

				select_list_AST = (AST)currentAST.root;

				select_list_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(SELECT_LIST,"select_list")).add(select_list_AST));

				currentAST.root = select_list_AST;

				currentAST.child = select_list_AST!=null &&select_list_AST.getFirstChild()!=null ?

					select_list_AST.getFirstChild() : select_list_AST;

				currentAST.advanceChildToEnd();

			}

			select_list_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_17);

			} else {

			  throw ex;

			}

		}

		returnAST = select_list_AST;

	}

	

	public final void table_reference_list() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST table_reference_list_AST = null;

		

		try {      // for error handling

			selected_table();

			astFactory.addASTChild(currentAST, returnAST);

			{

			_loop36:

			do {

				if ((LA(1)==COMMA)) {

					AST tmp16_AST = null;

					tmp16_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp16_AST);

					match(COMMA);

					selected_table();

					astFactory.addASTChild(currentAST, returnAST);

				}

				else {

					break _loop36;

				}

				

			} while (true);

			}

			if ( inputState.guessing==0 ) {

				table_reference_list_AST = (AST)currentAST.root;

				table_reference_list_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TABLE_REFERENCE_LIST,"table_reference_list")).add(table_reference_list_AST));

				currentAST.root = table_reference_list_AST;

				currentAST.child = table_reference_list_AST!=null &&table_reference_list_AST.getFirstChild()!=null ?

					table_reference_list_AST.getFirstChild() : table_reference_list_AST;

				currentAST.advanceChildToEnd();

			}

			table_reference_list_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_18);

			} else {

			  throw ex;

			}

		}

		returnAST = table_reference_list_AST;

	}

	

	public final void where_condition() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST where_condition_AST = null;

		

		try {      // for error handling

			condition();

			astFactory.addASTChild(currentAST, returnAST);

			if ( inputState.guessing==0 ) {

				where_condition_AST = (AST)currentAST.root;

				where_condition_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(WHERE_CONDITION,"where_condition")).add(where_condition_AST));

				currentAST.root = where_condition_AST;

				currentAST.child = where_condition_AST!=null &&where_condition_AST.getFirstChild()!=null ?

					where_condition_AST.getFirstChild() : where_condition_AST;

				currentAST.advanceChildToEnd();

			}

			where_condition_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_19);

			} else {

			  throw ex;

			}

		}

		returnAST = where_condition_AST;

	}

	

	public final void connect_clause() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST connect_clause_AST = null;

		

		try {      // for error handling

			{

			switch ( LA(1)) {

			case LITERAL_start:

			{

				AST tmp17_AST = null;

				tmp17_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp17_AST);

				match(LITERAL_start);

				AST tmp18_AST = null;

				tmp18_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp18_AST);

				match(LITERAL_with);

				condition();

				astFactory.addASTChild(currentAST, returnAST);

				break;

			}

			case LITERAL_connect:

			{

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			AST tmp19_AST = null;

			tmp19_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp19_AST);

			match(LITERAL_connect);

			AST tmp20_AST = null;

			tmp20_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp20_AST);

			match(LITERAL_by);

			condition();

			astFactory.addASTChild(currentAST, returnAST);

			{

			switch ( LA(1)) {

			case LITERAL_start:

			{

				AST tmp21_AST = null;

				tmp21_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp21_AST);

				match(LITERAL_start);

				AST tmp22_AST = null;

				tmp22_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp22_AST);

				match(LITERAL_with);

				condition();

				astFactory.addASTChild(currentAST, returnAST);

				break;

			}

			case EOF:

			case SEMI:

			case LITERAL_union:

			case OPEN_PAREN:

			case CLOSE_PAREN:

			case LITERAL_select:

			case LITERAL_group:

			case LITERAL_intersect:

			case LITERAL_minus:

			case LITERAL_order:

			case LITERAL_for:

			case LITERAL_update:

			case LITERAL_delete:

			{

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			connect_clause_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_20);

			} else {

			  throw ex;

			}

		}

		returnAST = connect_clause_AST;

	}

	

	public final void group_clause() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST group_clause_AST = null;

		

		try {      // for error handling

			AST tmp23_AST = null;

			tmp23_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp23_AST);

			match(LITERAL_group);

			AST tmp24_AST = null;

			tmp24_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp24_AST);

			match(LITERAL_by);

			expression();

			astFactory.addASTChild(currentAST, returnAST);

			{

			_loop187:

			do {

				if ((LA(1)==COMMA)) {

					AST tmp25_AST = null;

					tmp25_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp25_AST);

					match(COMMA);

					expression();

					astFactory.addASTChild(currentAST, returnAST);

				}

				else {

					break _loop187;

				}

				

			} while (true);

			}

			{

			switch ( LA(1)) {

			case LITERAL_having:

			{

				AST tmp26_AST = null;

				tmp26_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp26_AST);

				match(LITERAL_having);

				condition();

				astFactory.addASTChild(currentAST, returnAST);

				break;

			}

			case EOF:

			case SEMI:

			case LITERAL_union:

			case OPEN_PAREN:

			case CLOSE_PAREN:

			case LITERAL_select:

			case LITERAL_intersect:

			case LITERAL_minus:

			case LITERAL_order:

			case LITERAL_for:

			case LITERAL_update:

			case LITERAL_delete:

			{

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			group_clause_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_21);

			} else {

			  throw ex;

			}

		}

		returnAST = group_clause_AST;

	}

	

	public final void set_clause() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST set_clause_AST = null;

		

		try {      // for error handling

			{

			switch ( LA(1)) {

			case LITERAL_union:

			{

				{

				AST tmp27_AST = null;

				tmp27_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp27_AST);

				match(LITERAL_union);

				AST tmp28_AST = null;

				tmp28_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp28_AST);

				match(LITERAL_all);

				}

				break;

			}

			case LITERAL_intersect:

			{

				AST tmp29_AST = null;

				tmp29_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp29_AST);

				match(LITERAL_intersect);

				break;

			}

			case LITERAL_minus:

			{

				AST tmp30_AST = null;

				tmp30_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp30_AST);

				match(LITERAL_minus);

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			select_command();

			astFactory.addASTChild(currentAST, returnAST);

			set_clause_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_6);

			} else {

			  throw ex;

			}

		}

		returnAST = set_clause_AST;

	}

	

	public final void order_clause() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST order_clause_AST = null;

		

		try {      // for error handling

			AST tmp31_AST = null;

			tmp31_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp31_AST);

			match(LITERAL_order);

			AST tmp32_AST = null;

			tmp32_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp32_AST);

			match(LITERAL_by);

			sorted_def();

			astFactory.addASTChild(currentAST, returnAST);

			{

			_loop194:

			do {

				if ((LA(1)==COMMA)) {

					AST tmp33_AST = null;

					tmp33_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp33_AST);

					match(COMMA);

					sorted_def();

					astFactory.addASTChild(currentAST, returnAST);

				}

				else {

					break _loop194;

				}

				

			} while (true);

			}

			order_clause_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_6);

			} else {

			  throw ex;

			}

		}

		returnAST = order_clause_AST;

	}

	

	public final void update_clause() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST update_clause_AST = null;

		

		try {      // for error handling

			AST tmp34_AST = null;

			tmp34_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp34_AST);

			match(LITERAL_for);

			AST tmp35_AST = null;

			tmp35_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp35_AST);

			match(LITERAL_update);

			{

			switch ( LA(1)) {

			case LITERAL_of:

			{

				AST tmp36_AST = null;

				tmp36_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp36_AST);

				match(LITERAL_of);

				column_name();

				astFactory.addASTChild(currentAST, returnAST);

				{

				_loop205:

				do {

					if ((LA(1)==COMMA)) {

						AST tmp37_AST = null;

						tmp37_AST = astFactory.create(LT(1));

						astFactory.addASTChild(currentAST, tmp37_AST);

						match(COMMA);

						column_name();

						astFactory.addASTChild(currentAST, returnAST);

					}

					else {

						break _loop205;

					}

					

				} while (true);

				}

				break;

			}

			case EOF:

			case SEMI:

			case LITERAL_union:

			case OPEN_PAREN:

			case CLOSE_PAREN:

			case LITERAL_select:

			case LITERAL_order:

			case LITERAL_for:

			case LITERAL_update:

			case LITERAL_nowait:

			case LITERAL_delete:

			{

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			{

			switch ( LA(1)) {

			case LITERAL_nowait:

			{

				AST tmp38_AST = null;

				tmp38_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp38_AST);

				match(LITERAL_nowait);

				break;

			}

			case EOF:

			case SEMI:

			case LITERAL_union:

			case OPEN_PAREN:

			case CLOSE_PAREN:

			case LITERAL_select:

			case LITERAL_order:

			case LITERAL_for:

			case LITERAL_update:

			case LITERAL_delete:

			{

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			update_clause_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_6);

			} else {

			  throw ex;

			}

		}

		returnAST = update_clause_AST;

	}

	

	public final void displayed_column() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST displayed_column_AST = null;

		

		try {      // for error handling

			boolean synPredMatched41 = false;

			if (((_tokenSet_7.member(LA(1))) && (LA(2)==DOT) && (_tokenSet_22.member(LA(3))) && (LA(4)==LITERAL_from||LA(4)==COMMA||LA(4)==DOT))) {

				int _m41 = mark();

				synPredMatched41 = true;

				inputState.guessing++;

				try {

					{

					{

					if ((_tokenSet_7.member(LA(1))) && (LA(2)==DOT) && (_tokenSet_7.member(LA(3)))) {

						schema_name();

						match(DOT);

					}

					else if ((_tokenSet_7.member(LA(1))) && (LA(2)==DOT) && (LA(3)==ASTERISK)) {

					}

					else {

						throw new NoViableAltException(LT(1), getFilename());

					}

					

					}

					table_name();

					match(DOT);

					match(ASTERISK);

					}

				}

				catch (RecognitionException pe) {

					synPredMatched41 = false;

				}

				rewind(_m41);

				inputState.guessing--;

			}

			if ( synPredMatched41 ) {

				{

				{

				if ((_tokenSet_7.member(LA(1))) && (LA(2)==DOT) && (_tokenSet_7.member(LA(3)))) {

					schema_name();

					astFactory.addASTChild(currentAST, returnAST);

					AST tmp39_AST = null;

					tmp39_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp39_AST);

					match(DOT);

				}

				else if ((_tokenSet_7.member(LA(1))) && (LA(2)==DOT) && (LA(3)==ASTERISK)) {

				}

				else {

					throw new NoViableAltException(LT(1), getFilename());

				}

				

				}

				table_name();

				astFactory.addASTChild(currentAST, returnAST);

				AST tmp40_AST = null;

				tmp40_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp40_AST);

				match(DOT);

				AST tmp41_AST = null;

				tmp41_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp41_AST);

				match(ASTERISK);

				}

				displayed_column_AST = (AST)currentAST.root;

			}

			else if ((_tokenSet_14.member(LA(1))) && (_tokenSet_23.member(LA(2))) && (_tokenSet_24.member(LA(3))) && (_tokenSet_25.member(LA(4)))) {

				{

				exp_simple();

				astFactory.addASTChild(currentAST, returnAST);

				{

				switch ( LA(1)) {

				case LITERAL_as:

				case QUOTED_STRING:

				case LITERAL_abs:

				case LITERAL_ceil:

				case LITERAL_floor:

				case LITERAL_power:

				case LITERAL_round:

				case LITERAL_sign:

				case LITERAL_sqrt:

				case LITERAL_trunc:

				case LITERAL_chr:

				case LITERAL_initcap:

				case LITERAL_lower:

				case LITERAL_lpad:

				case LITERAL_ltrim:

				case LITERAL_replace:

				case LITERAL_rpad:

				case LITERAL_rtrim:

				case LITERAL_soundex:

				case LITERAL_substr:

				case LITERAL_translate:

				case LITERAL_upper:

				case LITERAL_ascii:

				case LITERAL_instr:

				case LITERAL_length:

				case LITERAL_concat:

				case LITERAL_count:

				case LITERAL_chartorowid:

				case LITERAL_convert:

				case LITERAL_hextoraw:

				case LITERAL_rawtohex:

				case LITERAL_rowidtochar:

				case LITERAL_to_char:

				case LITERAL_to_date:

				case LITERAL_to_number:

				case LITERAL_decode:

				case LITERAL_dump:

				case LITERAL_greatest:

				case LITERAL_least:

				case LITERAL_nvl:

				case LITERAL_userenv:

				case LITERAL_vsize:

				case LITERAL_user:

				case LITERAL_sysdate:

				case LITERAL_intersect:

				case IDENTIFIER:

				{

					alias();

					astFactory.addASTChild(currentAST, returnAST);

					break;

				}

				case LITERAL_from:

				case COMMA:

				{

					break;

				}

				default:

				{

					throw new NoViableAltException(LT(1), getFilename());

				}

				}

				}

				}

				displayed_column_AST = (AST)currentAST.root;

			}

			else {

				throw new NoViableAltException(LT(1), getFilename());

			}

			

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_26);

			} else {

			  throw ex;

			}

		}

		returnAST = displayed_column_AST;

	}

	

	public final void selected_table() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST selected_table_AST = null;

		

		try {      // for error handling

			{

			switch ( LA(1)) {

			case QUOTED_STRING:

			case LITERAL_abs:

			case LITERAL_ceil:

			case LITERAL_floor:

			case LITERAL_power:

			case LITERAL_round:

			case LITERAL_sign:

			case LITERAL_sqrt:

			case LITERAL_trunc:

			case LITERAL_chr:

			case LITERAL_initcap:

			case LITERAL_lower:

			case LITERAL_lpad:

			case LITERAL_ltrim:

			case LITERAL_replace:

			case LITERAL_rpad:

			case LITERAL_rtrim:

			case LITERAL_soundex:

			case LITERAL_substr:

			case LITERAL_translate:

			case LITERAL_upper:

			case LITERAL_ascii:

			case LITERAL_instr:

			case LITERAL_length:

			case LITERAL_concat:

			case LITERAL_count:

			case LITERAL_chartorowid:

			case LITERAL_convert:

			case LITERAL_hextoraw:

			case LITERAL_rawtohex:

			case LITERAL_rowidtochar:

			case LITERAL_to_char:

			case LITERAL_to_date:

			case LITERAL_to_number:

			case LITERAL_decode:

			case LITERAL_dump:

			case LITERAL_greatest:

			case LITERAL_least:

			case LITERAL_nvl:

			case LITERAL_userenv:

			case LITERAL_vsize:

			case LITERAL_user:

			case LITERAL_sysdate:

			case LITERAL_intersect:

			case IDENTIFIER:

			{

				table_spec();

				astFactory.addASTChild(currentAST, returnAST);

				break;

			}

			case OPEN_PAREN:

			{

				subquery();

				astFactory.addASTChild(currentAST, returnAST);

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			{

			if ((_tokenSet_27.member(LA(1))) && (_tokenSet_28.member(LA(2))) && (_tokenSet_29.member(LA(3))) && (_tokenSet_30.member(LA(4)))) {

				alias();

				astFactory.addASTChild(currentAST, returnAST);

			}

			else if ((_tokenSet_31.member(LA(1))) && (_tokenSet_29.member(LA(2))) && (_tokenSet_30.member(LA(3))) && (_tokenSet_32.member(LA(4)))) {

			}

			else {

				throw new NoViableAltException(LT(1), getFilename());

			}

			

			}

			selected_table_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_31);

			} else {

			  throw ex;

			}

		}

		returnAST = selected_table_AST;

	}

	

	public final void condition() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST condition_AST = null;

		

		try {      // for error handling

			logical_term();

			astFactory.addASTChild(currentAST, returnAST);

			{

			_loop134:

			do {

				if ((LA(1)==LITERAL_or) && (_tokenSet_33.member(LA(2))) && (_tokenSet_34.member(LA(3))) && (_tokenSet_35.member(LA(4)))) {

					AST tmp42_AST = null;

					tmp42_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp42_AST);

					match(LITERAL_or);

					logical_term();

					astFactory.addASTChild(currentAST, returnAST);

				}

				else {

					break _loop134;

				}

				

			} while (true);

			}

			condition_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_36);

			} else {

			  throw ex;

			}

		}

		returnAST = condition_AST;

	}

	

	public final void schema_name() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST schema_name_AST = null;

		

		try {      // for error handling

			identifier();

			astFactory.addASTChild(currentAST, returnAST);

			schema_name_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_37);

			} else {

			  throw ex;

			}

		}

		returnAST = schema_name_AST;

	}

	

	public final void table_name() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST table_name_AST = null;

		

		try {      // for error handling

			identifier();

			astFactory.addASTChild(currentAST, returnAST);

			table_name_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_38);

			} else {

			  throw ex;

			}

		}

		returnAST = table_name_AST;

	}

	

	public final void exp_simple() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST exp_simple_AST = null;

		

		try {      // for error handling

			expression();

			astFactory.addASTChild(currentAST, returnAST);

			exp_simple_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_39);

			} else {

			  throw ex;

			}

		}

		returnAST = exp_simple_AST;

	}

	

	public final void alias() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST alias_AST = null;

		

		try {      // for error handling

			{

			switch ( LA(1)) {

			case LITERAL_as:

			{

				AST tmp43_AST = null;

				tmp43_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp43_AST);

				match(LITERAL_as);

				break;

			}

			case QUOTED_STRING:

			case LITERAL_abs:

			case LITERAL_ceil:

			case LITERAL_floor:

			case LITERAL_power:

			case LITERAL_round:

			case LITERAL_sign:

			case LITERAL_sqrt:

			case LITERAL_trunc:

			case LITERAL_chr:

			case LITERAL_initcap:

			case LITERAL_lower:

			case LITERAL_lpad:

			case LITERAL_ltrim:

			case LITERAL_replace:

			case LITERAL_rpad:

			case LITERAL_rtrim:

			case LITERAL_soundex:

			case LITERAL_substr:

			case LITERAL_translate:

			case LITERAL_upper:

			case LITERAL_ascii:

			case LITERAL_instr:

			case LITERAL_length:

			case LITERAL_concat:

			case LITERAL_count:

			case LITERAL_chartorowid:

			case LITERAL_convert:

			case LITERAL_hextoraw:

			case LITERAL_rawtohex:

			case LITERAL_rowidtochar:

			case LITERAL_to_char:

			case LITERAL_to_date:

			case LITERAL_to_number:

			case LITERAL_decode:

			case LITERAL_dump:

			case LITERAL_greatest:

			case LITERAL_least:

			case LITERAL_nvl:

			case LITERAL_userenv:

			case LITERAL_vsize:

			case LITERAL_user:

			case LITERAL_sysdate:

			case LITERAL_intersect:

			case IDENTIFIER:

			{

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			identifier();

			astFactory.addASTChild(currentAST, returnAST);

			alias_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_40);

			} else {

			  throw ex;

			}

		}

		returnAST = alias_AST;

	}

	

	public final void identifier() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST identifier_AST = null;

		

		try {      // for error handling

			{

			switch ( LA(1)) {

			case IDENTIFIER:

			{

				AST tmp44_AST = null;

				tmp44_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp44_AST);

				match(IDENTIFIER);

				break;

			}

			case QUOTED_STRING:

			{

				AST tmp45_AST = null;

				tmp45_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp45_AST);

				match(QUOTED_STRING);

				break;

			}

			case LITERAL_abs:

			case LITERAL_ceil:

			case LITERAL_floor:

			case LITERAL_power:

			case LITERAL_round:

			case LITERAL_sign:

			case LITERAL_sqrt:

			case LITERAL_trunc:

			case LITERAL_chr:

			case LITERAL_initcap:

			case LITERAL_lower:

			case LITERAL_lpad:

			case LITERAL_ltrim:

			case LITERAL_replace:

			case LITERAL_rpad:

			case LITERAL_rtrim:

			case LITERAL_soundex:

			case LITERAL_substr:

			case LITERAL_translate:

			case LITERAL_upper:

			case LITERAL_ascii:

			case LITERAL_instr:

			case LITERAL_length:

			case LITERAL_concat:

			case LITERAL_count:

			case LITERAL_chartorowid:

			case LITERAL_convert:

			case LITERAL_hextoraw:

			case LITERAL_rawtohex:

			case LITERAL_rowidtochar:

			case LITERAL_to_char:

			case LITERAL_to_date:

			case LITERAL_to_number:

			case LITERAL_decode:

			case LITERAL_dump:

			case LITERAL_greatest:

			case LITERAL_least:

			case LITERAL_nvl:

			case LITERAL_userenv:

			case LITERAL_vsize:

			case LITERAL_user:

			case LITERAL_sysdate:

			case LITERAL_intersect:

			{

				keyword();

				astFactory.addASTChild(currentAST, returnAST);

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			if ( inputState.guessing==0 ) {

				identifier_AST = (AST)currentAST.root;

				identifier_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(SQL_IDENTIFIER,"sql_identifier")).add(identifier_AST));

				currentAST.root = identifier_AST;

				currentAST.child = identifier_AST!=null &&identifier_AST.getFirstChild()!=null ?

					identifier_AST.getFirstChild() : identifier_AST;

				currentAST.advanceChildToEnd();

			}

			identifier_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_41);

			} else {

			  throw ex;

			}

		}

		returnAST = identifier_AST;

	}

	

	public final void expression() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST expression_AST = null;

		

		try {      // for error handling

			term();

			astFactory.addASTChild(currentAST, returnAST);

			{

			_loop52:

			do {

				if ((LA(1)==PLUS||LA(1)==MINUS) && (_tokenSet_14.member(LA(2))) && (_tokenSet_42.member(LA(3))) && (_tokenSet_43.member(LA(4)))) {

					{

					switch ( LA(1)) {

					case PLUS:

					{

						AST tmp46_AST = null;

						tmp46_AST = astFactory.create(LT(1));

						astFactory.addASTChild(currentAST, tmp46_AST);

						match(PLUS);

						break;

					}

					case MINUS:

					{

						AST tmp47_AST = null;

						tmp47_AST = astFactory.create(LT(1));

						astFactory.addASTChild(currentAST, tmp47_AST);

						match(MINUS);

						break;

					}

					default:

					{

						throw new NoViableAltException(LT(1), getFilename());

					}

					}

					}

					term();

					astFactory.addASTChild(currentAST, returnAST);

				}

				else {

					break _loop52;

				}

				

			} while (true);

			}

			expression_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_44);

			} else {

			  throw ex;

			}

		}

		returnAST = expression_AST;

	}

	

	public final void term() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST term_AST = null;

		

		try {      // for error handling

			factor();

			astFactory.addASTChild(currentAST, returnAST);

			{

			_loop58:

			do {

				if ((LA(1)==ASTERISK||LA(1)==DIVIDE) && (_tokenSet_14.member(LA(2))) && (_tokenSet_42.member(LA(3))) && (_tokenSet_43.member(LA(4)))) {

					{

					switch ( LA(1)) {

					case ASTERISK:

					{

						multiply();

						astFactory.addASTChild(currentAST, returnAST);

						break;

					}

					case DIVIDE:

					{

						AST tmp48_AST = null;

						tmp48_AST = astFactory.create(LT(1));

						astFactory.addASTChild(currentAST, tmp48_AST);

						match(DIVIDE);

						break;

					}

					default:

					{

						throw new NoViableAltException(LT(1), getFilename());

					}

					}

					}

					factor();

					astFactory.addASTChild(currentAST, returnAST);

				}

				else {

					break _loop58;

				}

				

			} while (true);

			}

			term_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_44);

			} else {

			  throw ex;

			}

		}

		returnAST = term_AST;

	}

	

	public final void factor() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST factor_AST = null;

		

		try {      // for error handling

			factor2();

			astFactory.addASTChild(currentAST, returnAST);

			{

			_loop62:

			do {

				if ((LA(1)==VERTBAR) && (LA(2)==VERTBAR) && (_tokenSet_14.member(LA(3))) && (_tokenSet_42.member(LA(4)))) {

					AST tmp49_AST = null;

					tmp49_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp49_AST);

					match(VERTBAR);

					AST tmp50_AST = null;

					tmp50_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp50_AST);

					match(VERTBAR);

					factor2();

					astFactory.addASTChild(currentAST, returnAST);

				}

				else {

					break _loop62;

				}

				

			} while (true);

			}

			factor_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_44);

			} else {

			  throw ex;

			}

		}

		returnAST = factor_AST;

	}

	

	public final void multiply() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST multiply_AST = null;

		

		try {      // for error handling

			AST tmp51_AST = null;

			tmp51_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp51_AST);

			match(ASTERISK);

			if ( inputState.guessing==0 ) {

				multiply_AST = (AST)currentAST.root;

				multiply_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(MULTIPLY,"multiply")).add(multiply_AST));

				currentAST.root = multiply_AST;

				currentAST.child = multiply_AST!=null &&multiply_AST.getFirstChild()!=null ?

					multiply_AST.getFirstChild() : multiply_AST;

				currentAST.advanceChildToEnd();

			}

			multiply_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_14);

			} else {

			  throw ex;

			}

		}

		returnAST = multiply_AST;

	}

	

	public final void factor2() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST factor2_AST = null;

		

		try {      // for error handling

			boolean synPredMatched65 = false;

			if ((((LA(1) >= NUMBER && LA(1) <= BIND_NAME)) && (_tokenSet_44.member(LA(2))) && (_tokenSet_45.member(LA(3))) && (_tokenSet_46.member(LA(4))))) {

				int _m65 = mark();

				synPredMatched65 = true;

				inputState.guessing++;

				try {

					{

					sql_literal();

					}

				}

				catch (RecognitionException pe) {

					synPredMatched65 = false;

				}

				rewind(_m65);

				inputState.guessing--;

			}

			if ( synPredMatched65 ) {

				sql_literal();

				astFactory.addASTChild(currentAST, returnAST);

				factor2_AST = (AST)currentAST.root;

			}

			else {

				boolean synPredMatched68 = false;

				if (((LA(1)==PLUS||LA(1)==MINUS))) {

					int _m68 = mark();

					synPredMatched68 = true;

					inputState.guessing++;

					try {

						{

						{

						switch ( LA(1)) {

						case PLUS:

						{

							match(PLUS);

							break;

						}

						case MINUS:

						{

							match(MINUS);

							break;

						}

						default:

						{

							throw new NoViableAltException(LT(1), getFilename());

						}

						}

						}

						expression();

						}

					}

					catch (RecognitionException pe) {

						synPredMatched68 = false;

					}

					rewind(_m68);

					inputState.guessing--;

				}

				if ( synPredMatched68 ) {

					{

					switch ( LA(1)) {

					case PLUS:

					{

						AST tmp52_AST = null;

						tmp52_AST = astFactory.create(LT(1));

						astFactory.addASTChild(currentAST, tmp52_AST);

						match(PLUS);

						break;

					}

					case MINUS:

					{

						AST tmp53_AST = null;

						tmp53_AST = astFactory.create(LT(1));

						astFactory.addASTChild(currentAST, tmp53_AST);

						match(MINUS);

						break;

					}

					default:

					{

						throw new NoViableAltException(LT(1), getFilename());

					}

					}

					}

					expression();

					astFactory.addASTChild(currentAST, returnAST);

					factor2_AST = (AST)currentAST.root;

				}

				else {

					boolean synPredMatched74 = false;

					if (((_tokenSet_47.member(LA(1))) && (LA(2)==OPEN_PAREN) && (_tokenSet_14.member(LA(3))) && (_tokenSet_48.member(LA(4))))) {

						int _m74 = mark();

						synPredMatched74 = true;

						inputState.guessing++;

						try {

							{

							function();

							{

							match(OPEN_PAREN);

							expression();

							{

							_loop73:

							do {

								if ((LA(1)==COMMA)) {

									match(COMMA);

									expression();

								}

								else {

									break _loop73;

								}

								

							} while (true);

							}

							match(CLOSE_PAREN);

							}

							}

						}

						catch (RecognitionException pe) {

							synPredMatched74 = false;

						}

						rewind(_m74);

						inputState.guessing--;

					}

					if ( synPredMatched74 ) {

						function();

						astFactory.addASTChild(currentAST, returnAST);

						{

						AST tmp54_AST = null;

						tmp54_AST = astFactory.create(LT(1));

						astFactory.addASTChild(currentAST, tmp54_AST);

						match(OPEN_PAREN);

						expression();

						astFactory.addASTChild(currentAST, returnAST);

						{

						_loop77:

						do {

							if ((LA(1)==COMMA)) {

								AST tmp55_AST = null;

								tmp55_AST = astFactory.create(LT(1));

								astFactory.addASTChild(currentAST, tmp55_AST);

								match(COMMA);

								expression();

								astFactory.addASTChild(currentAST, returnAST);

							}

							else {

								break _loop77;

							}

							

						} while (true);

						}

						AST tmp56_AST = null;

						tmp56_AST = astFactory.create(LT(1));

						astFactory.addASTChild(currentAST, tmp56_AST);

						match(CLOSE_PAREN);

						}

						if ( inputState.guessing==0 ) {

							factor2_AST = (AST)currentAST.root;

							factor2_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(FUNCTION,"function")).add(factor2_AST));

							currentAST.root = factor2_AST;

							currentAST.child = factor2_AST!=null &&factor2_AST.getFirstChild()!=null ?

								factor2_AST.getFirstChild() : factor2_AST;

							currentAST.advanceChildToEnd();

						}

						factor2_AST = (AST)currentAST.root;

					}

					else {

						boolean synPredMatched81 = false;

						if ((((LA(1) >= LITERAL_avg && LA(1) <= LITERAL_variance)) && (LA(2)==OPEN_PAREN) && (_tokenSet_49.member(LA(3))) && (_tokenSet_42.member(LA(4))))) {

							int _m81 = mark();

							synPredMatched81 = true;

							inputState.guessing++;

							try {

								{

								group_function();

								match(OPEN_PAREN);

								{

								switch ( LA(1)) {

								case ASTERISK:

								{

									match(ASTERISK);

									break;

								}

								case LITERAL_all:

								{

									match(LITERAL_all);

									break;

								}

								case LITERAL_distinct:

								{

									match(LITERAL_distinct);

									break;

								}

								case OPEN_PAREN:

								case CLOSE_PAREN:

								case PLUS:

								case MINUS:

								case NUMBER:

								case QUOTED_STRING:

								case LITERAL_null:

								case QUESTION_MARK:

								case BIND_NAME:

								case LITERAL_abs:

								case LITERAL_ceil:

								case LITERAL_floor:

								case LITERAL_mod:

								case LITERAL_power:

								case LITERAL_round:

								case LITERAL_sign:

								case LITERAL_sqrt:

								case LITERAL_trunc:

								case LITERAL_chr:

								case LITERAL_initcap:

								case LITERAL_lower:

								case LITERAL_lpad:

								case LITERAL_ltrim:

								case LITERAL_replace:

								case LITERAL_rpad:

								case LITERAL_rtrim:

								case LITERAL_soundex:

								case LITERAL_substr:

								case LITERAL_translate:

								case LITERAL_upper:

								case LITERAL_ascii:

								case LITERAL_instr:

								case LITERAL_length:

								case LITERAL_concat:

								case LITERAL_avg:

								case LITERAL_count:

								case LITERAL_max:

								case LITERAL_min:

								case LITERAL_stddev:

								case LITERAL_sum:

								case LITERAL_variance:

								case LITERAL_chartorowid:

								case LITERAL_convert:

								case LITERAL_hextoraw:

								case LITERAL_rawtohex:

								case LITERAL_rowidtochar:

								case LITERAL_to_char:

								case LITERAL_to_date:

								case LITERAL_to_number:

								case LITERAL_decode:

								case LITERAL_dump:

								case LITERAL_greatest:

								case LITERAL_least:

								case LITERAL_nvl:

								case LITERAL_uid:

								case LITERAL_userenv:

								case LITERAL_vsize:

								case LITERAL_user:

								case LITERAL_sysdate:

								case LITERAL_intersect:

								case IDENTIFIER:

								{

									break;

								}

								default:

								{

									throw new NoViableAltException(LT(1), getFilename());

								}

								}

								}

								{

								switch ( LA(1)) {

								case OPEN_PAREN:

								case PLUS:

								case MINUS:

								case NUMBER:

								case QUOTED_STRING:

								case LITERAL_null:

								case QUESTION_MARK:

								case BIND_NAME:

								case LITERAL_abs:

								case LITERAL_ceil:

								case LITERAL_floor:

								case LITERAL_mod:

								case LITERAL_power:

								case LITERAL_round:

								case LITERAL_sign:

								case LITERAL_sqrt:

								case LITERAL_trunc:

								case LITERAL_chr:

								case LITERAL_initcap:

								case LITERAL_lower:

								case LITERAL_lpad:

								case LITERAL_ltrim:

								case LITERAL_replace:

								case LITERAL_rpad:

								case LITERAL_rtrim:

								case LITERAL_soundex:

								case LITERAL_substr:

								case LITERAL_translate:

								case LITERAL_upper:

								case LITERAL_ascii:

								case LITERAL_instr:

								case LITERAL_length:

								case LITERAL_concat:

								case LITERAL_avg:

								case LITERAL_count:

								case LITERAL_max:

								case LITERAL_min:

								case LITERAL_stddev:

								case LITERAL_sum:

								case LITERAL_variance:

								case LITERAL_chartorowid:

								case LITERAL_convert:

								case LITERAL_hextoraw:

								case LITERAL_rawtohex:

								case LITERAL_rowidtochar:

								case LITERAL_to_char:

								case LITERAL_to_date:

								case LITERAL_to_number:

								case LITERAL_decode:

								case LITERAL_dump:

								case LITERAL_greatest:

								case LITERAL_least:

								case LITERAL_nvl:

								case LITERAL_uid:

								case LITERAL_userenv:

								case LITERAL_vsize:

								case LITERAL_user:

								case LITERAL_sysdate:

								case LITERAL_intersect:

								case IDENTIFIER:

								{

									expression();

									break;

								}

								case CLOSE_PAREN:

								{

									break;

								}

								default:

								{

									throw new NoViableAltException(LT(1), getFilename());

								}

								}

								}

								match(CLOSE_PAREN);

								}

							}

							catch (RecognitionException pe) {

								synPredMatched81 = false;

							}

							rewind(_m81);

							inputState.guessing--;

						}

						if ( synPredMatched81 ) {

							group_function();

							astFactory.addASTChild(currentAST, returnAST);

							AST tmp57_AST = null;

							tmp57_AST = astFactory.create(LT(1));

							astFactory.addASTChild(currentAST, tmp57_AST);

							match(OPEN_PAREN);

							{

							switch ( LA(1)) {

							case ASTERISK:

							{

								AST tmp58_AST = null;

								tmp58_AST = astFactory.create(LT(1));

								astFactory.addASTChild(currentAST, tmp58_AST);

								match(ASTERISK);

								break;

							}

							case LITERAL_all:

							{

								AST tmp59_AST = null;

								tmp59_AST = astFactory.create(LT(1));

								astFactory.addASTChild(currentAST, tmp59_AST);

								match(LITERAL_all);

								break;

							}

							case LITERAL_distinct:

							{

								AST tmp60_AST = null;

								tmp60_AST = astFactory.create(LT(1));

								astFactory.addASTChild(currentAST, tmp60_AST);

								match(LITERAL_distinct);

								break;

							}

							case OPEN_PAREN:

							case CLOSE_PAREN:

							case PLUS:

							case MINUS:

							case NUMBER:

							case QUOTED_STRING:

							case LITERAL_null:

							case QUESTION_MARK:

							case BIND_NAME:

							case LITERAL_abs:

							case LITERAL_ceil:

							case LITERAL_floor:

							case LITERAL_mod:

							case LITERAL_power:

							case LITERAL_round:

							case LITERAL_sign:

							case LITERAL_sqrt:

							case LITERAL_trunc:

							case LITERAL_chr:

							case LITERAL_initcap:

							case LITERAL_lower:

							case LITERAL_lpad:

							case LITERAL_ltrim:

							case LITERAL_replace:

							case LITERAL_rpad:

							case LITERAL_rtrim:

							case LITERAL_soundex:

							case LITERAL_substr:

							case LITERAL_translate:

							case LITERAL_upper:

							case LITERAL_ascii:

							case LITERAL_instr:

							case LITERAL_length:

							case LITERAL_concat:

							case LITERAL_avg:

							case LITERAL_count:

							case LITERAL_max:

							case LITERAL_min:

							case LITERAL_stddev:

							case LITERAL_sum:

							case LITERAL_variance:

							case LITERAL_chartorowid:

							case LITERAL_convert:

							case LITERAL_hextoraw:

							case LITERAL_rawtohex:

							case LITERAL_rowidtochar:

							case LITERAL_to_char:

							case LITERAL_to_date:

							case LITERAL_to_number:

							case LITERAL_decode:

							case LITERAL_dump:

							case LITERAL_greatest:

							case LITERAL_least:

							case LITERAL_nvl:

							case LITERAL_uid:

							case LITERAL_userenv:

							case LITERAL_vsize:

							case LITERAL_user:

							case LITERAL_sysdate:

							case LITERAL_intersect:

							case IDENTIFIER:

							{

								break;

							}

							default:

							{

								throw new NoViableAltException(LT(1), getFilename());

							}

							}

							}

							{

							switch ( LA(1)) {

							case OPEN_PAREN:

							case PLUS:

							case MINUS:

							case NUMBER:

							case QUOTED_STRING:

							case LITERAL_null:

							case QUESTION_MARK:

							case BIND_NAME:

							case LITERAL_abs:

							case LITERAL_ceil:

							case LITERAL_floor:

							case LITERAL_mod:

							case LITERAL_power:

							case LITERAL_round:

							case LITERAL_sign:

							case LITERAL_sqrt:

							case LITERAL_trunc:

							case LITERAL_chr:

							case LITERAL_initcap:

							case LITERAL_lower:

							case LITERAL_lpad:

							case LITERAL_ltrim:

							case LITERAL_replace:

							case LITERAL_rpad:

							case LITERAL_rtrim:

							case LITERAL_soundex:

							case LITERAL_substr:

							case LITERAL_translate:

							case LITERAL_upper:

							case LITERAL_ascii:

							case LITERAL_instr:

							case LITERAL_length:

							case LITERAL_concat:

							case LITERAL_avg:

							case LITERAL_count:

							case LITERAL_max:

							case LITERAL_min:

							case LITERAL_stddev:

							case LITERAL_sum:

							case LITERAL_variance:

							case LITERAL_chartorowid:

							case LITERAL_convert:

							case LITERAL_hextoraw:

							case LITERAL_rawtohex:

							case LITERAL_rowidtochar:

							case LITERAL_to_char:

							case LITERAL_to_date:

							case LITERAL_to_number:

							case LITERAL_decode:

							case LITERAL_dump:

							case LITERAL_greatest:

							case LITERAL_least:

							case LITERAL_nvl:

							case LITERAL_uid:

							case LITERAL_userenv:

							case LITERAL_vsize:

							case LITERAL_user:

							case LITERAL_sysdate:

							case LITERAL_intersect:

							case IDENTIFIER:

							{

								expression();

								astFactory.addASTChild(currentAST, returnAST);

								break;

							}

							case CLOSE_PAREN:

							{

								break;

							}

							default:

							{

								throw new NoViableAltException(LT(1), getFilename());

							}

							}

							}

							AST tmp61_AST = null;

							tmp61_AST = astFactory.create(LT(1));

							astFactory.addASTChild(currentAST, tmp61_AST);

							match(CLOSE_PAREN);

							if ( inputState.guessing==0 ) {

								factor2_AST = (AST)currentAST.root;

								factor2_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(GROUP_FUNCTION,"group_function")).add(factor2_AST));

								currentAST.root = factor2_AST;

								currentAST.child = factor2_AST!=null &&factor2_AST.getFirstChild()!=null ?

									factor2_AST.getFirstChild() : factor2_AST;

								currentAST.advanceChildToEnd();

							}

							factor2_AST = (AST)currentAST.root;

						}

						else {

							boolean synPredMatched88 = false;

							if (((_tokenSet_7.member(LA(1))) && (LA(2)==OPEN_PAREN||LA(2)==DOT) && (_tokenSet_14.member(LA(3))) && (_tokenSet_48.member(LA(4))))) {

								int _m88 = mark();

								synPredMatched88 = true;

								inputState.guessing++;

								try {

									{

									user_defined_function();

									{

									match(OPEN_PAREN);

									expression();

									{

									_loop87:

									do {

										if ((LA(1)==COMMA)) {

											match(COMMA);

											expression();

										}

										else {

											break _loop87;

										}

										

									} while (true);

									}

									match(CLOSE_PAREN);

									}

									}

								}

								catch (RecognitionException pe) {

									synPredMatched88 = false;

								}

								rewind(_m88);

								inputState.guessing--;

							}

							if ( synPredMatched88 ) {

								user_defined_function();

								astFactory.addASTChild(currentAST, returnAST);

								{

								AST tmp62_AST = null;

								tmp62_AST = astFactory.create(LT(1));

								astFactory.addASTChild(currentAST, tmp62_AST);

								match(OPEN_PAREN);

								expression();

								astFactory.addASTChild(currentAST, returnAST);

								{

								_loop91:

								do {

									if ((LA(1)==COMMA)) {

										AST tmp63_AST = null;

										tmp63_AST = astFactory.create(LT(1));

										astFactory.addASTChild(currentAST, tmp63_AST);

										match(COMMA);

										expression();

										astFactory.addASTChild(currentAST, returnAST);

									}

									else {

										break _loop91;

									}

									

								} while (true);

								}

								AST tmp64_AST = null;

								tmp64_AST = astFactory.create(LT(1));

								astFactory.addASTChild(currentAST, tmp64_AST);

								match(CLOSE_PAREN);

								}

								if ( inputState.guessing==0 ) {

									factor2_AST = (AST)currentAST.root;

									factor2_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(USER_FUNCTION,"user_function")).add(factor2_AST));

									currentAST.root = factor2_AST;

									currentAST.child = factor2_AST!=null &&factor2_AST.getFirstChild()!=null ?

										factor2_AST.getFirstChild() : factor2_AST;

									currentAST.advanceChildToEnd();

								}

								factor2_AST = (AST)currentAST.root;

							}

							else {

								boolean synPredMatched93 = false;

								if (((LA(1)==OPEN_PAREN) && (_tokenSet_14.member(LA(2))) && (_tokenSet_50.member(LA(3))) && (_tokenSet_51.member(LA(4))))) {

									int _m93 = mark();

									synPredMatched93 = true;

									inputState.guessing++;

									try {

										{

										match(OPEN_PAREN);

										expression();

										match(CLOSE_PAREN);

										}

									}

									catch (RecognitionException pe) {

										synPredMatched93 = false;

									}

									rewind(_m93);

									inputState.guessing--;

								}

								if ( synPredMatched93 ) {

									AST tmp65_AST = null;

									tmp65_AST = astFactory.create(LT(1));

									astFactory.addASTChild(currentAST, tmp65_AST);

									match(OPEN_PAREN);

									expression();

									astFactory.addASTChild(currentAST, returnAST);

									AST tmp66_AST = null;

									tmp66_AST = astFactory.create(LT(1));

									astFactory.addASTChild(currentAST, tmp66_AST);

									match(CLOSE_PAREN);

									factor2_AST = (AST)currentAST.root;

								}

								else {

									boolean synPredMatched95 = false;

									if (((_tokenSet_7.member(LA(1))) && (_tokenSet_52.member(LA(2))) && (_tokenSet_45.member(LA(3))) && (_tokenSet_46.member(LA(4))))) {

										int _m95 = mark();

										synPredMatched95 = true;

										inputState.guessing++;

										try {

											{

											variable();

											}

										}

										catch (RecognitionException pe) {

											synPredMatched95 = false;

										}

										rewind(_m95);

										inputState.guessing--;

									}

									if ( synPredMatched95 ) {

										variable();

										astFactory.addASTChild(currentAST, returnAST);

										factor2_AST = (AST)currentAST.root;

									}

									else if ((LA(1)==OPEN_PAREN) && (_tokenSet_14.member(LA(2))) && (_tokenSet_53.member(LA(3))) && (_tokenSet_54.member(LA(4)))) {

										expression_list();

										astFactory.addASTChild(currentAST, returnAST);

										factor2_AST = (AST)currentAST.root;

									}

									else {

										throw new NoViableAltException(LT(1), getFilename());

									}

									}}}}}}

								}

								catch (RecognitionException ex) {

									if (inputState.guessing==0) {

										reportError(ex);

										consume();

										consumeUntil(_tokenSet_44);

									} else {

									  throw ex;

									}

								}

								returnAST = factor2_AST;

							}

							

	public final void sql_literal() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST sql_literal_AST = null;

		

		try {      // for error handling

			{

			switch ( LA(1)) {

			case NUMBER:

			{

				AST tmp67_AST = null;

				tmp67_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp67_AST);

				match(NUMBER);

				break;

			}

			case QUOTED_STRING:

			{

				AST tmp68_AST = null;

				tmp68_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp68_AST);

				match(QUOTED_STRING);

				break;

			}

			case LITERAL_null:

			{

				AST tmp69_AST = null;

				tmp69_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp69_AST);

				match(LITERAL_null);

				break;

			}

			case QUESTION_MARK:

			{

				AST tmp70_AST = null;

				tmp70_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp70_AST);

				match(QUESTION_MARK);

				break;

			}

			case BIND_NAME:

			{

				AST tmp71_AST = null;

				tmp71_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp71_AST);

				match(BIND_NAME);

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			if ( inputState.guessing==0 ) {

				sql_literal_AST = (AST)currentAST.root;

				sql_literal_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(SQL_LITERAL,"sql_literal")).add(sql_literal_AST));

				currentAST.root = sql_literal_AST;

				currentAST.child = sql_literal_AST!=null &&sql_literal_AST.getFirstChild()!=null ?

					sql_literal_AST.getFirstChild() : sql_literal_AST;

				currentAST.advanceChildToEnd();

			}

			sql_literal_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_44);

			} else {

			  throw ex;

			}

		}

		returnAST = sql_literal_AST;

	}

	

	public final void function() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST function_AST = null;

		

		try {      // for error handling

			switch ( LA(1)) {

			case LITERAL_abs:

			case LITERAL_ceil:

			case LITERAL_floor:

			case LITERAL_mod:

			case LITERAL_power:

			case LITERAL_round:

			case LITERAL_sign:

			case LITERAL_sqrt:

			case LITERAL_trunc:

			{

				number_function();

				astFactory.addASTChild(currentAST, returnAST);

				function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_chr:

			case LITERAL_initcap:

			case LITERAL_lower:

			case LITERAL_lpad:

			case LITERAL_ltrim:

			case LITERAL_replace:

			case LITERAL_rpad:

			case LITERAL_rtrim:

			case LITERAL_soundex:

			case LITERAL_substr:

			case LITERAL_translate:

			case LITERAL_upper:

			case LITERAL_ascii:

			case LITERAL_instr:

			case LITERAL_length:

			case LITERAL_concat:

			{

				char_function();

				astFactory.addASTChild(currentAST, returnAST);

				function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_chartorowid:

			case LITERAL_convert:

			case LITERAL_hextoraw:

			case LITERAL_rawtohex:

			case LITERAL_rowidtochar:

			case LITERAL_to_char:

			case LITERAL_to_date:

			case LITERAL_to_number:

			{

				conversion_function();

				astFactory.addASTChild(currentAST, returnAST);

				function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_decode:

			case LITERAL_dump:

			case LITERAL_greatest:

			case LITERAL_least:

			case LITERAL_nvl:

			case LITERAL_uid:

			case LITERAL_userenv:

			case LITERAL_vsize:

			{

				other_function();

				astFactory.addASTChild(currentAST, returnAST);

				function_AST = (AST)currentAST.root;

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_55);

			} else {

			  throw ex;

			}

		}

		returnAST = function_AST;

	}

	

	public final void group_function() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST group_function_AST = null;

		

		try {      // for error handling

			switch ( LA(1)) {

			case LITERAL_avg:

			{

				AST tmp72_AST = null;

				tmp72_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp72_AST);

				match(LITERAL_avg);

				group_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_count:

			{

				AST tmp73_AST = null;

				tmp73_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp73_AST);

				match(LITERAL_count);

				group_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_max:

			{

				AST tmp74_AST = null;

				tmp74_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp74_AST);

				match(LITERAL_max);

				group_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_min:

			{

				AST tmp75_AST = null;

				tmp75_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp75_AST);

				match(LITERAL_min);

				group_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_stddev:

			{

				AST tmp76_AST = null;

				tmp76_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp76_AST);

				match(LITERAL_stddev);

				group_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_sum:

			{

				AST tmp77_AST = null;

				tmp77_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp77_AST);

				match(LITERAL_sum);

				group_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_variance:

			{

				AST tmp78_AST = null;

				tmp78_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp78_AST);

				match(LITERAL_variance);

				group_function_AST = (AST)currentAST.root;

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_55);

			} else {

			  throw ex;

			}

		}

		returnAST = group_function_AST;

	}

	

	public final void user_defined_function() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST user_defined_function_AST = null;

		

		try {      // for error handling

			{

			if ((_tokenSet_7.member(LA(1))) && (LA(2)==DOT)) {

				{

				if ((_tokenSet_7.member(LA(1))) && (LA(2)==DOT) && (_tokenSet_7.member(LA(3))) && (LA(4)==DOT)) {

					schema_name();

					astFactory.addASTChild(currentAST, returnAST);

					AST tmp79_AST = null;

					tmp79_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp79_AST);

					match(DOT);

				}

				else if ((_tokenSet_7.member(LA(1))) && (LA(2)==DOT) && (_tokenSet_7.member(LA(3))) && (LA(4)==OPEN_PAREN)) {

				}

				else {

					throw new NoViableAltException(LT(1), getFilename());

				}

				

				}

				package_name();

				astFactory.addASTChild(currentAST, returnAST);

				AST tmp80_AST = null;

				tmp80_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp80_AST);

				match(DOT);

			}

			else if ((_tokenSet_7.member(LA(1))) && (LA(2)==OPEN_PAREN)) {

			}

			else {

				throw new NoViableAltException(LT(1), getFilename());

			}

			

			}

			identifier();

			astFactory.addASTChild(currentAST, returnAST);

			user_defined_function_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_55);

			} else {

			  throw ex;

			}

		}

		returnAST = user_defined_function_AST;

	}

	

	public final void variable() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST variable_AST = null;

		

		try {      // for error handling

			boolean synPredMatched104 = false;

			if (((_tokenSet_7.member(LA(1))) && (LA(2)==OPEN_PAREN||LA(2)==DOT) && (_tokenSet_56.member(LA(3))) && (LA(4)==OPEN_PAREN||LA(4)==CLOSE_PAREN||LA(4)==DOT))) {

				int _m104 = mark();

				synPredMatched104 = true;

				inputState.guessing++;

				try {

					{

					column_spec();

					{

					match(OPEN_PAREN);

					match(PLUS);

					match(CLOSE_PAREN);

					}

					}

				}

				catch (RecognitionException pe) {

					synPredMatched104 = false;

				}

				rewind(_m104);

				inputState.guessing--;

			}

			if ( synPredMatched104 ) {

				column_spec();

				astFactory.addASTChild(currentAST, returnAST);

				{

				AST tmp81_AST = null;

				tmp81_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp81_AST);

				match(OPEN_PAREN);

				AST tmp82_AST = null;

				tmp82_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp82_AST);

				match(PLUS);

				AST tmp83_AST = null;

				tmp83_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp83_AST);

				match(CLOSE_PAREN);

				}

				variable_AST = (AST)currentAST.root;

			}

			else if ((_tokenSet_7.member(LA(1))) && (_tokenSet_52.member(LA(2))) && (_tokenSet_45.member(LA(3))) && (_tokenSet_46.member(LA(4)))) {

				column_spec();

				astFactory.addASTChild(currentAST, returnAST);

				variable_AST = (AST)currentAST.root;

			}

			else {

				throw new NoViableAltException(LT(1), getFilename());

			}

			

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_44);

			} else {

			  throw ex;

			}

		}

		returnAST = variable_AST;

	}

	

	public final void expression_list() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST expression_list_AST = null;

		

		try {      // for error handling

			AST tmp84_AST = null;

			tmp84_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp84_AST);

			match(OPEN_PAREN);

			expression();

			astFactory.addASTChild(currentAST, returnAST);

			{

			int _cnt98=0;

			_loop98:

			do {

				if ((LA(1)==COMMA)) {

					AST tmp85_AST = null;

					tmp85_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp85_AST);

					match(COMMA);

					expression();

					astFactory.addASTChild(currentAST, returnAST);

				}

				else {

					if ( _cnt98>=1 ) { break _loop98; } else {throw new NoViableAltException(LT(1), getFilename());}

				}

				

				_cnt98++;

			} while (true);

			}

			AST tmp86_AST = null;

			tmp86_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp86_AST);

			match(CLOSE_PAREN);

			expression_list_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_44);

			} else {

			  throw ex;

			}

		}

		returnAST = expression_list_AST;

	}

	

	public final void column_spec() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST column_spec_AST = null;

		

		try {      // for error handling

			{

			if ((_tokenSet_7.member(LA(1))) && (LA(2)==DOT)) {

				{

				if ((_tokenSet_7.member(LA(1))) && (LA(2)==DOT) && (_tokenSet_7.member(LA(3))) && (LA(4)==DOT)) {

					schema_name();

					astFactory.addASTChild(currentAST, returnAST);

					AST tmp87_AST = null;

					tmp87_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp87_AST);

					match(DOT);

				}

				else if ((_tokenSet_7.member(LA(1))) && (LA(2)==DOT) && (_tokenSet_7.member(LA(3))) && (_tokenSet_44.member(LA(4)))) {

				}

				else {

					throw new NoViableAltException(LT(1), getFilename());

				}

				

				}

				table_name();

				astFactory.addASTChild(currentAST, returnAST);

				AST tmp88_AST = null;

				tmp88_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp88_AST);

				match(DOT);

			}

			else if ((_tokenSet_7.member(LA(1))) && (_tokenSet_44.member(LA(2)))) {

			}

			else {

				throw new NoViableAltException(LT(1), getFilename());

			}

			

			}

			column_name();

			astFactory.addASTChild(currentAST, returnAST);

			column_spec_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_44);

			} else {

			  throw ex;

			}

		}

		returnAST = column_spec_AST;

	}

	

	public final void column_name() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST column_name_AST = null;

		

		try {      // for error handling

			identifier();

			astFactory.addASTChild(currentAST, returnAST);

			column_name_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_57);

			} else {

			  throw ex;

			}

		}

		returnAST = column_name_AST;

	}

	

	public final void package_name() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST package_name_AST = null;

		

		try {      // for error handling

			identifier();

			astFactory.addASTChild(currentAST, returnAST);

			package_name_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_37);

			} else {

			  throw ex;

			}

		}

		returnAST = package_name_AST;

	}

	

	public final void number_function() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST number_function_AST = null;

		

		try {      // for error handling

			switch ( LA(1)) {

			case LITERAL_abs:

			{

				AST tmp89_AST = null;

				tmp89_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp89_AST);

				match(LITERAL_abs);

				number_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_ceil:

			{

				AST tmp90_AST = null;

				tmp90_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp90_AST);

				match(LITERAL_ceil);

				number_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_floor:

			{

				AST tmp91_AST = null;

				tmp91_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp91_AST);

				match(LITERAL_floor);

				number_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_mod:

			{

				AST tmp92_AST = null;

				tmp92_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp92_AST);

				match(LITERAL_mod);

				number_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_power:

			{

				AST tmp93_AST = null;

				tmp93_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp93_AST);

				match(LITERAL_power);

				number_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_round:

			{

				AST tmp94_AST = null;

				tmp94_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp94_AST);

				match(LITERAL_round);

				number_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_sign:

			{

				AST tmp95_AST = null;

				tmp95_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp95_AST);

				match(LITERAL_sign);

				number_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_sqrt:

			{

				AST tmp96_AST = null;

				tmp96_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp96_AST);

				match(LITERAL_sqrt);

				number_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_trunc:

			{

				AST tmp97_AST = null;

				tmp97_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp97_AST);

				match(LITERAL_trunc);

				number_function_AST = (AST)currentAST.root;

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_55);

			} else {

			  throw ex;

			}

		}

		returnAST = number_function_AST;

	}

	

	public final void char_function() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST char_function_AST = null;

		

		try {      // for error handling

			switch ( LA(1)) {

			case LITERAL_chr:

			{

				AST tmp98_AST = null;

				tmp98_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp98_AST);

				match(LITERAL_chr);

				char_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_initcap:

			{

				AST tmp99_AST = null;

				tmp99_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp99_AST);

				match(LITERAL_initcap);

				char_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_lower:

			{

				AST tmp100_AST = null;

				tmp100_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp100_AST);

				match(LITERAL_lower);

				char_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_lpad:

			{

				AST tmp101_AST = null;

				tmp101_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp101_AST);

				match(LITERAL_lpad);

				char_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_ltrim:

			{

				AST tmp102_AST = null;

				tmp102_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp102_AST);

				match(LITERAL_ltrim);

				char_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_replace:

			{

				AST tmp103_AST = null;

				tmp103_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp103_AST);

				match(LITERAL_replace);

				char_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_rpad:

			{

				AST tmp104_AST = null;

				tmp104_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp104_AST);

				match(LITERAL_rpad);

				char_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_rtrim:

			{

				AST tmp105_AST = null;

				tmp105_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp105_AST);

				match(LITERAL_rtrim);

				char_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_soundex:

			{

				AST tmp106_AST = null;

				tmp106_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp106_AST);

				match(LITERAL_soundex);

				char_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_substr:

			{

				AST tmp107_AST = null;

				tmp107_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp107_AST);

				match(LITERAL_substr);

				char_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_translate:

			{

				AST tmp108_AST = null;

				tmp108_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp108_AST);

				match(LITERAL_translate);

				char_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_upper:

			{

				AST tmp109_AST = null;

				tmp109_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp109_AST);

				match(LITERAL_upper);

				char_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_ascii:

			{

				AST tmp110_AST = null;

				tmp110_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp110_AST);

				match(LITERAL_ascii);

				char_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_instr:

			{

				AST tmp111_AST = null;

				tmp111_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp111_AST);

				match(LITERAL_instr);

				char_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_length:

			{

				AST tmp112_AST = null;

				tmp112_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp112_AST);

				match(LITERAL_length);

				char_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_concat:

			{

				AST tmp113_AST = null;

				tmp113_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp113_AST);

				match(LITERAL_concat);

				char_function_AST = (AST)currentAST.root;

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_55);

			} else {

			  throw ex;

			}

		}

		returnAST = char_function_AST;

	}

	

	public final void conversion_function() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST conversion_function_AST = null;

		

		try {      // for error handling

			switch ( LA(1)) {

			case LITERAL_chartorowid:

			{

				AST tmp114_AST = null;

				tmp114_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp114_AST);

				match(LITERAL_chartorowid);

				conversion_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_convert:

			{

				AST tmp115_AST = null;

				tmp115_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp115_AST);

				match(LITERAL_convert);

				conversion_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_hextoraw:

			{

				AST tmp116_AST = null;

				tmp116_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp116_AST);

				match(LITERAL_hextoraw);

				conversion_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_rawtohex:

			{

				AST tmp117_AST = null;

				tmp117_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp117_AST);

				match(LITERAL_rawtohex);

				conversion_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_rowidtochar:

			{

				AST tmp118_AST = null;

				tmp118_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp118_AST);

				match(LITERAL_rowidtochar);

				conversion_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_to_char:

			{

				AST tmp119_AST = null;

				tmp119_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp119_AST);

				match(LITERAL_to_char);

				conversion_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_to_date:

			{

				AST tmp120_AST = null;

				tmp120_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp120_AST);

				match(LITERAL_to_date);

				conversion_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_to_number:

			{

				AST tmp121_AST = null;

				tmp121_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp121_AST);

				match(LITERAL_to_number);

				conversion_function_AST = (AST)currentAST.root;

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_55);

			} else {

			  throw ex;

			}

		}

		returnAST = conversion_function_AST;

	}

	

	public final void other_function() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST other_function_AST = null;

		

		try {      // for error handling

			switch ( LA(1)) {

			case LITERAL_decode:

			{

				AST tmp122_AST = null;

				tmp122_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp122_AST);

				match(LITERAL_decode);

				other_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_dump:

			{

				AST tmp123_AST = null;

				tmp123_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp123_AST);

				match(LITERAL_dump);

				other_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_greatest:

			{

				AST tmp124_AST = null;

				tmp124_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp124_AST);

				match(LITERAL_greatest);

				other_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_least:

			{

				AST tmp125_AST = null;

				tmp125_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp125_AST);

				match(LITERAL_least);

				other_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_nvl:

			{

				AST tmp126_AST = null;

				tmp126_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp126_AST);

				match(LITERAL_nvl);

				other_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_uid:

			{

				AST tmp127_AST = null;

				tmp127_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp127_AST);

				match(LITERAL_uid);

				other_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_userenv:

			{

				AST tmp128_AST = null;

				tmp128_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp128_AST);

				match(LITERAL_userenv);

				other_function_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_vsize:

			{

				AST tmp129_AST = null;

				tmp129_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp129_AST);

				match(LITERAL_vsize);

				other_function_AST = (AST)currentAST.root;

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_55);

			} else {

			  throw ex;

			}

		}

		returnAST = other_function_AST;

	}

	

	public final void pseudo_column() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST pseudo_column_AST = null;

		

		try {      // for error handling

			switch ( LA(1)) {

			case LITERAL_user:

			{

				AST tmp130_AST = null;

				tmp130_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp130_AST);

				match(LITERAL_user);

				pseudo_column_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_sysdate:

			{

				AST tmp131_AST = null;

				tmp131_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp131_AST);

				match(LITERAL_sysdate);

				pseudo_column_AST = (AST)currentAST.root;

				break;

			}

			case 87:

			{

				AST tmp132_AST = null;

				tmp132_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp132_AST);

				match(87);

				pseudo_column_AST = (AST)currentAST.root;

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_1);

			} else {

			  throw ex;

			}

		}

		returnAST = pseudo_column_AST;

	}

	

	public final void table_spec() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST table_spec_AST = null;

		

		try {      // for error handling

			{

			if ((_tokenSet_7.member(LA(1))) && (LA(2)==DOT)) {

				schema_name();

				astFactory.addASTChild(currentAST, returnAST);

				AST tmp133_AST = null;

				tmp133_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp133_AST);

				match(DOT);

			}

			else if ((_tokenSet_7.member(LA(1))) && (_tokenSet_58.member(LA(2)))) {

			}

			else {

				throw new NoViableAltException(LT(1), getFilename());

			}

			

			}

			table_name();

			astFactory.addASTChild(currentAST, returnAST);

			{

			switch ( LA(1)) {

			case AT_SIGN:

			{

				AST tmp134_AST = null;

				tmp134_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp134_AST);

				match(AT_SIGN);

				link_name();

				astFactory.addASTChild(currentAST, returnAST);

				break;

			}

			case EOF:

			case SEMI:

			case LITERAL_union:

			case OPEN_PAREN:

			case CLOSE_PAREN:

			case LITERAL_select:

			case LITERAL_where:

			case COMMA:

			case LITERAL_as:

			case QUOTED_STRING:

			case LITERAL_abs:

			case LITERAL_ceil:

			case LITERAL_floor:

			case LITERAL_power:

			case LITERAL_round:

			case LITERAL_sign:

			case LITERAL_sqrt:

			case LITERAL_trunc:

			case LITERAL_chr:

			case LITERAL_initcap:

			case LITERAL_lower:

			case LITERAL_lpad:

			case LITERAL_ltrim:

			case LITERAL_replace:

			case LITERAL_rpad:

			case LITERAL_rtrim:

			case LITERAL_soundex:

			case LITERAL_substr:

			case LITERAL_translate:

			case LITERAL_upper:

			case LITERAL_ascii:

			case LITERAL_instr:

			case LITERAL_length:

			case LITERAL_concat:

			case LITERAL_count:

			case LITERAL_chartorowid:

			case LITERAL_convert:

			case LITERAL_hextoraw:

			case LITERAL_rawtohex:

			case LITERAL_rowidtochar:

			case LITERAL_to_char:

			case LITERAL_to_date:

			case LITERAL_to_number:

			case LITERAL_decode:

			case LITERAL_dump:

			case LITERAL_greatest:

			case LITERAL_least:

			case LITERAL_nvl:

			case LITERAL_userenv:

			case LITERAL_vsize:

			case LITERAL_user:

			case LITERAL_sysdate:

			case LITERAL_start:

			case LITERAL_connect:

			case LITERAL_group:

			case LITERAL_intersect:

			case LITERAL_minus:

			case LITERAL_order:

			case LITERAL_for:

			case LITERAL_update:

			case LITERAL_delete:

			case IDENTIFIER:

			{

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			table_spec_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_59);

			} else {

			  throw ex;

			}

		}

		returnAST = table_spec_AST;

	}

	

	public final void subquery() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST subquery_AST = null;

		

		try {      // for error handling

			AST tmp135_AST = null;

			tmp135_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp135_AST);

			match(OPEN_PAREN);

			select_command();

			astFactory.addASTChild(currentAST, returnAST);

			AST tmp136_AST = null;

			tmp136_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp136_AST);

			match(CLOSE_PAREN);

			if ( inputState.guessing==0 ) {

				subquery_AST = (AST)currentAST.root;

				subquery_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(SUBQUERY,"subquery")).add(subquery_AST));

				currentAST.root = subquery_AST;

				currentAST.child = subquery_AST!=null &&subquery_AST.getFirstChild()!=null ?

					subquery_AST.getFirstChild() : subquery_AST;

				currentAST.advanceChildToEnd();

			}

			subquery_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_60);

			} else {

			  throw ex;

			}

		}

		returnAST = subquery_AST;

	}

	

	public final void link_name() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST link_name_AST = null;

		

		try {      // for error handling

			identifier();

			astFactory.addASTChild(currentAST, returnAST);

			link_name_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_61);

			} else {

			  throw ex;

			}

		}

		returnAST = link_name_AST;

	}

	

	public final void table_alias() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST table_alias_AST = null;

		

		try {      // for error handling

			{

			if ((_tokenSet_7.member(LA(1))) && (LA(2)==DOT)) {

				schema_name();

				astFactory.addASTChild(currentAST, returnAST);

				AST tmp137_AST = null;

				tmp137_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp137_AST);

				match(DOT);

			}

			else if ((_tokenSet_7.member(LA(1))) && (_tokenSet_62.member(LA(2)))) {

			}

			else {

				throw new NoViableAltException(LT(1), getFilename());

			}

			

			}

			table_name();

			astFactory.addASTChild(currentAST, returnAST);

			{

			switch ( LA(1)) {

			case AT_SIGN:

			{

				AST tmp138_AST = null;

				tmp138_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp138_AST);

				match(AT_SIGN);

				link_name();

				astFactory.addASTChild(currentAST, returnAST);

				break;

			}

			case EOF:

			case SEMI:

			case OPEN_PAREN:

			case LITERAL_select:

			case LITERAL_where:

			case LITERAL_as:

			case QUOTED_STRING:

			case LITERAL_abs:

			case LITERAL_ceil:

			case LITERAL_floor:

			case LITERAL_power:

			case LITERAL_round:

			case LITERAL_sign:

			case LITERAL_sqrt:

			case LITERAL_trunc:

			case LITERAL_chr:

			case LITERAL_initcap:

			case LITERAL_lower:

			case LITERAL_lpad:

			case LITERAL_ltrim:

			case LITERAL_replace:

			case LITERAL_rpad:

			case LITERAL_rtrim:

			case LITERAL_soundex:

			case LITERAL_substr:

			case LITERAL_translate:

			case LITERAL_upper:

			case LITERAL_ascii:

			case LITERAL_instr:

			case LITERAL_length:

			case LITERAL_concat:

			case LITERAL_count:

			case LITERAL_chartorowid:

			case LITERAL_convert:

			case LITERAL_hextoraw:

			case LITERAL_rawtohex:

			case LITERAL_rowidtochar:

			case LITERAL_to_char:

			case LITERAL_to_date:

			case LITERAL_to_number:

			case LITERAL_decode:

			case LITERAL_dump:

			case LITERAL_greatest:

			case LITERAL_least:

			case LITERAL_nvl:

			case LITERAL_userenv:

			case LITERAL_vsize:

			case LITERAL_user:

			case LITERAL_sysdate:

			case LITERAL_intersect:

			case LITERAL_update:

			case LITERAL_delete:

			case LITERAL_set:

			case IDENTIFIER:

			{

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			{

			switch ( LA(1)) {

			case LITERAL_as:

			case QUOTED_STRING:

			case LITERAL_abs:

			case LITERAL_ceil:

			case LITERAL_floor:

			case LITERAL_power:

			case LITERAL_round:

			case LITERAL_sign:

			case LITERAL_sqrt:

			case LITERAL_trunc:

			case LITERAL_chr:

			case LITERAL_initcap:

			case LITERAL_lower:

			case LITERAL_lpad:

			case LITERAL_ltrim:

			case LITERAL_replace:

			case LITERAL_rpad:

			case LITERAL_rtrim:

			case LITERAL_soundex:

			case LITERAL_substr:

			case LITERAL_translate:

			case LITERAL_upper:

			case LITERAL_ascii:

			case LITERAL_instr:

			case LITERAL_length:

			case LITERAL_concat:

			case LITERAL_count:

			case LITERAL_chartorowid:

			case LITERAL_convert:

			case LITERAL_hextoraw:

			case LITERAL_rawtohex:

			case LITERAL_rowidtochar:

			case LITERAL_to_char:

			case LITERAL_to_date:

			case LITERAL_to_number:

			case LITERAL_decode:

			case LITERAL_dump:

			case LITERAL_greatest:

			case LITERAL_least:

			case LITERAL_nvl:

			case LITERAL_userenv:

			case LITERAL_vsize:

			case LITERAL_user:

			case LITERAL_sysdate:

			case LITERAL_intersect:

			case IDENTIFIER:

			{

				alias();

				astFactory.addASTChild(currentAST, returnAST);

				break;

			}

			case EOF:

			case SEMI:

			case OPEN_PAREN:

			case LITERAL_select:

			case LITERAL_where:

			case LITERAL_update:

			case LITERAL_delete:

			case LITERAL_set:

			{

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

			}

			table_alias_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_63);

			} else {

			  throw ex;

			}

		}

		returnAST = table_alias_AST;

	}

	

	public final void logical_term() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST logical_term_AST = null;

		

		try {      // for error handling

			logical_factor();

			astFactory.addASTChild(currentAST, returnAST);

			{

			_loop137:

			do {

				if ((LA(1)==LITERAL_and) && (_tokenSet_33.member(LA(2))) && (_tokenSet_34.member(LA(3))) && (_tokenSet_35.member(LA(4)))) {

					AST tmp139_AST = null;

					tmp139_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp139_AST);

					match(LITERAL_and);

					logical_factor();

					astFactory.addASTChild(currentAST, returnAST);

				}

				else {

					break _loop137;

				}

				

			} while (true);

			}

			logical_term_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_36);

			} else {

			  throw ex;

			}

		}

		returnAST = logical_term_AST;

	}

	

	public final void logical_factor() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST logical_factor_AST = null;

		

		try {      // for error handling

			boolean synPredMatched142 = false;

			if (((_tokenSet_64.member(LA(1))) && (_tokenSet_65.member(LA(2))) && (_tokenSet_66.member(LA(3))) && (_tokenSet_67.member(LA(4))))) {

				int _m142 = mark();

				synPredMatched142 = true;

				inputState.guessing++;

				try {

					{

					{

					switch ( LA(1)) {

					case LITERAL_prior:

					{

						match(LITERAL_prior);

						break;

					}

					case OPEN_PAREN:

					case PLUS:

					case MINUS:

					case NUMBER:

					case QUOTED_STRING:

					case LITERAL_null:

					case QUESTION_MARK:

					case BIND_NAME:

					case LITERAL_abs:

					case LITERAL_ceil:

					case LITERAL_floor:

					case LITERAL_mod:

					case LITERAL_power:

					case LITERAL_round:

					case LITERAL_sign:

					case LITERAL_sqrt:

					case LITERAL_trunc:

					case LITERAL_chr:

					case LITERAL_initcap:

					case LITERAL_lower:

					case LITERAL_lpad:

					case LITERAL_ltrim:

					case LITERAL_replace:

					case LITERAL_rpad:

					case LITERAL_rtrim:

					case LITERAL_soundex:

					case LITERAL_substr:

					case LITERAL_translate:

					case LITERAL_upper:

					case LITERAL_ascii:

					case LITERAL_instr:

					case LITERAL_length:

					case LITERAL_concat:

					case LITERAL_avg:

					case LITERAL_count:

					case LITERAL_max:

					case LITERAL_min:

					case LITERAL_stddev:

					case LITERAL_sum:

					case LITERAL_variance:

					case LITERAL_chartorowid:

					case LITERAL_convert:

					case LITERAL_hextoraw:

					case LITERAL_rawtohex:

					case LITERAL_rowidtochar:

					case LITERAL_to_char:

					case LITERAL_to_date:

					case LITERAL_to_number:

					case LITERAL_decode:

					case LITERAL_dump:

					case LITERAL_greatest:

					case LITERAL_least:

					case LITERAL_nvl:

					case LITERAL_uid:

					case LITERAL_userenv:

					case LITERAL_vsize:

					case LITERAL_user:

					case LITERAL_sysdate:

					case LITERAL_intersect:

					case IDENTIFIER:

					{

						break;

					}

					default:

					{

						throw new NoViableAltException(LT(1), getFilename());

					}

					}

					}

					exp_simple();

					comparison_op();

					{

					switch ( LA(1)) {

					case LITERAL_prior:

					{

						match(LITERAL_prior);

						break;

					}

					case OPEN_PAREN:

					case PLUS:

					case MINUS:

					case NUMBER:

					case QUOTED_STRING:

					case LITERAL_null:

					case QUESTION_MARK:

					case BIND_NAME:

					case LITERAL_abs:

					case LITERAL_ceil:

					case LITERAL_floor:

					case LITERAL_mod:

					case LITERAL_power:

					case LITERAL_round:

					case LITERAL_sign:

					case LITERAL_sqrt:

					case LITERAL_trunc:

					case LITERAL_chr:

					case LITERAL_initcap:

					case LITERAL_lower:

					case LITERAL_lpad:

					case LITERAL_ltrim:

					case LITERAL_replace:

					case LITERAL_rpad:

					case LITERAL_rtrim:

					case LITERAL_soundex:

					case LITERAL_substr:

					case LITERAL_translate:

					case LITERAL_upper:

					case LITERAL_ascii:

					case LITERAL_instr:

					case LITERAL_length:

					case LITERAL_concat:

					case LITERAL_avg:

					case LITERAL_count:

					case LITERAL_max:

					case LITERAL_min:

					case LITERAL_stddev:

					case LITERAL_sum:

					case LITERAL_variance:

					case LITERAL_chartorowid:

					case LITERAL_convert:

					case LITERAL_hextoraw:

					case LITERAL_rawtohex:

					case LITERAL_rowidtochar:

					case LITERAL_to_char:

					case LITERAL_to_date:

					case LITERAL_to_number:

					case LITERAL_decode:

					case LITERAL_dump:

					case LITERAL_greatest:

					case LITERAL_least:

					case LITERAL_nvl:

					case LITERAL_uid:

					case LITERAL_userenv:

					case LITERAL_vsize:

					case LITERAL_user:

					case LITERAL_sysdate:

					case LITERAL_intersect:

					case IDENTIFIER:

					{

						break;

					}

					default:

					{

						throw new NoViableAltException(LT(1), getFilename());

					}

					}

					}

					exp_simple();

					}

				}

				catch (RecognitionException pe) {

					synPredMatched142 = false;

				}

				rewind(_m142);

				inputState.guessing--;

			}

			if ( synPredMatched142 ) {

				{

				{

				switch ( LA(1)) {

				case LITERAL_prior:

				{

					AST tmp140_AST = null;

					tmp140_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp140_AST);

					match(LITERAL_prior);

					break;

				}

				case OPEN_PAREN:

				case PLUS:

				case MINUS:

				case NUMBER:

				case QUOTED_STRING:

				case LITERAL_null:

				case QUESTION_MARK:

				case BIND_NAME:

				case LITERAL_abs:

				case LITERAL_ceil:

				case LITERAL_floor:

				case LITERAL_mod:

				case LITERAL_power:

				case LITERAL_round:

				case LITERAL_sign:

				case LITERAL_sqrt:

				case LITERAL_trunc:

				case LITERAL_chr:

				case LITERAL_initcap:

				case LITERAL_lower:

				case LITERAL_lpad:

				case LITERAL_ltrim:

				case LITERAL_replace:

				case LITERAL_rpad:

				case LITERAL_rtrim:

				case LITERAL_soundex:

				case LITERAL_substr:

				case LITERAL_translate:

				case LITERAL_upper:

				case LITERAL_ascii:

				case LITERAL_instr:

				case LITERAL_length:

				case LITERAL_concat:

				case LITERAL_avg:

				case LITERAL_count:

				case LITERAL_max:

				case LITERAL_min:

				case LITERAL_stddev:

				case LITERAL_sum:

				case LITERAL_variance:

				case LITERAL_chartorowid:

				case LITERAL_convert:

				case LITERAL_hextoraw:

				case LITERAL_rawtohex:

				case LITERAL_rowidtochar:

				case LITERAL_to_char:

				case LITERAL_to_date:

				case LITERAL_to_number:

				case LITERAL_decode:

				case LITERAL_dump:

				case LITERAL_greatest:

				case LITERAL_least:

				case LITERAL_nvl:

				case LITERAL_uid:

				case LITERAL_userenv:

				case LITERAL_vsize:

				case LITERAL_user:

				case LITERAL_sysdate:

				case LITERAL_intersect:

				case IDENTIFIER:

				{

					break;

				}

				default:

				{

					throw new NoViableAltException(LT(1), getFilename());

				}

				}

				}

				exp_simple();

				astFactory.addASTChild(currentAST, returnAST);

				comparison_op();

				astFactory.addASTChild(currentAST, returnAST);

				{

				switch ( LA(1)) {

				case LITERAL_prior:

				{

					AST tmp141_AST = null;

					tmp141_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp141_AST);

					match(LITERAL_prior);

					break;

				}

				case OPEN_PAREN:

				case PLUS:

				case MINUS:

				case NUMBER:

				case QUOTED_STRING:

				case LITERAL_null:

				case QUESTION_MARK:

				case BIND_NAME:

				case LITERAL_abs:

				case LITERAL_ceil:

				case LITERAL_floor:

				case LITERAL_mod:

				case LITERAL_power:

				case LITERAL_round:

				case LITERAL_sign:

				case LITERAL_sqrt:

				case LITERAL_trunc:

				case LITERAL_chr:

				case LITERAL_initcap:

				case LITERAL_lower:

				case LITERAL_lpad:

				case LITERAL_ltrim:

				case LITERAL_replace:

				case LITERAL_rpad:

				case LITERAL_rtrim:

				case LITERAL_soundex:

				case LITERAL_substr:

				case LITERAL_translate:

				case LITERAL_upper:

				case LITERAL_ascii:

				case LITERAL_instr:

				case LITERAL_length:

				case LITERAL_concat:

				case LITERAL_avg:

				case LITERAL_count:

				case LITERAL_max:

				case LITERAL_min:

				case LITERAL_stddev:

				case LITERAL_sum:

				case LITERAL_variance:

				case LITERAL_chartorowid:

				case LITERAL_convert:

				case LITERAL_hextoraw:

				case LITERAL_rawtohex:

				case LITERAL_rowidtochar:

				case LITERAL_to_char:

				case LITERAL_to_date:

				case LITERAL_to_number:

				case LITERAL_decode:

				case LITERAL_dump:

				case LITERAL_greatest:

				case LITERAL_least:

				case LITERAL_nvl:

				case LITERAL_uid:

				case LITERAL_userenv:

				case LITERAL_vsize:

				case LITERAL_user:

				case LITERAL_sysdate:

				case LITERAL_intersect:

				case IDENTIFIER:

				{

					break;

				}

				default:

				{

					throw new NoViableAltException(LT(1), getFilename());

				}

				}

				}

				exp_simple();

				astFactory.addASTChild(currentAST, returnAST);

				}

				logical_factor_AST = (AST)currentAST.root;

			}

			else {

				boolean synPredMatched148 = false;

				if (((_tokenSet_14.member(LA(1))) && (_tokenSet_68.member(LA(2))) && (_tokenSet_69.member(LA(3))) && (_tokenSet_70.member(LA(4))))) {

					int _m148 = mark();

					synPredMatched148 = true;

					inputState.guessing++;

					try {

						{

						exp_simple();

						{

						switch ( LA(1)) {

						case LITERAL_not:

						{

							match(LITERAL_not);

							break;

						}

						case LITERAL_in:

						{

							break;

						}

						default:

						{

							throw new NoViableAltException(LT(1), getFilename());

						}

						}

						}

						match(LITERAL_in);

						}

					}

					catch (RecognitionException pe) {

						synPredMatched148 = false;

					}

					rewind(_m148);

					inputState.guessing--;

				}

				if ( synPredMatched148 ) {

					exp_simple();

					astFactory.addASTChild(currentAST, returnAST);

					{

					switch ( LA(1)) {

					case LITERAL_not:

					{

						AST tmp142_AST = null;

						tmp142_AST = astFactory.create(LT(1));

						astFactory.addASTChild(currentAST, tmp142_AST);

						match(LITERAL_not);

						break;

					}

					case LITERAL_in:

					{

						break;

					}

					default:

					{

						throw new NoViableAltException(LT(1), getFilename());

					}

					}

					}

					AST tmp143_AST = null;

					tmp143_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp143_AST);

					match(LITERAL_in);

					exp_set();

					astFactory.addASTChild(currentAST, returnAST);

					logical_factor_AST = (AST)currentAST.root;

				}

				else {

					boolean synPredMatched152 = false;

					if (((_tokenSet_14.member(LA(1))) && (_tokenSet_71.member(LA(2))) && (_tokenSet_72.member(LA(3))) && (_tokenSet_73.member(LA(4))))) {

						int _m152 = mark();

						synPredMatched152 = true;

						inputState.guessing++;

						try {

							{

							exp_simple();

							{

							switch ( LA(1)) {

							case LITERAL_not:

							{

								match(LITERAL_not);

								break;

							}

							case LITERAL_like:

							{

								break;

							}

							default:

							{

								throw new NoViableAltException(LT(1), getFilename());

							}

							}

							}

							match(LITERAL_like);

							}

						}

						catch (RecognitionException pe) {

							synPredMatched152 = false;

						}

						rewind(_m152);

						inputState.guessing--;

					}

					if ( synPredMatched152 ) {

						exp_simple();

						astFactory.addASTChild(currentAST, returnAST);

						{

						switch ( LA(1)) {

						case LITERAL_not:

						{

							AST tmp144_AST = null;

							tmp144_AST = astFactory.create(LT(1));

							astFactory.addASTChild(currentAST, tmp144_AST);

							match(LITERAL_not);

							break;

						}

						case LITERAL_like:

						{

							break;

						}

						default:

						{

							throw new NoViableAltException(LT(1), getFilename());

						}

						}

						}

						AST tmp145_AST = null;

						tmp145_AST = astFactory.create(LT(1));

						astFactory.addASTChild(currentAST, tmp145_AST);

						match(LITERAL_like);

						expression();

						astFactory.addASTChild(currentAST, returnAST);

						{

						switch ( LA(1)) {

						case LITERAL_escape:

						{

							AST tmp146_AST = null;

							tmp146_AST = astFactory.create(LT(1));

							astFactory.addASTChild(currentAST, tmp146_AST);

							match(LITERAL_escape);

							AST tmp147_AST = null;

							tmp147_AST = astFactory.create(LT(1));

							astFactory.addASTChild(currentAST, tmp147_AST);

							match(QUOTED_STRING);

							break;

						}

						case EOF:

						case SEMI:

						case LITERAL_union:

						case OPEN_PAREN:

						case CLOSE_PAREN:

						case LITERAL_select:

						case LITERAL_or:

						case LITERAL_and:

						case LITERAL_start:

						case LITERAL_connect:

						case LITERAL_group:

						case LITERAL_intersect:

						case LITERAL_minus:

						case LITERAL_order:

						case LITERAL_for:

						case LITERAL_update:

						case LITERAL_delete:

						{

							break;

						}

						default:

						{

							throw new NoViableAltException(LT(1), getFilename());

						}

						}

						}

						logical_factor_AST = (AST)currentAST.root;

					}

					else {

						boolean synPredMatched157 = false;

						if (((_tokenSet_14.member(LA(1))) && (_tokenSet_74.member(LA(2))) && (_tokenSet_75.member(LA(3))) && (_tokenSet_76.member(LA(4))))) {

							int _m157 = mark();

							synPredMatched157 = true;

							inputState.guessing++;

							try {

								{

								exp_simple();

								{

								switch ( LA(1)) {

								case LITERAL_not:

								{

									match(LITERAL_not);

									break;

								}

								case LITERAL_between:

								{

									break;

								}

								default:

								{

									throw new NoViableAltException(LT(1), getFilename());

								}

								}

								}

								match(LITERAL_between);

								}

							}

							catch (RecognitionException pe) {

								synPredMatched157 = false;

							}

							rewind(_m157);

							inputState.guessing--;

						}

						if ( synPredMatched157 ) {

							exp_simple();

							astFactory.addASTChild(currentAST, returnAST);

							{

							switch ( LA(1)) {

							case LITERAL_not:

							{

								AST tmp148_AST = null;

								tmp148_AST = astFactory.create(LT(1));

								astFactory.addASTChild(currentAST, tmp148_AST);

								match(LITERAL_not);

								break;

							}

							case LITERAL_between:

							{

								break;

							}

							default:

							{

								throw new NoViableAltException(LT(1), getFilename());

							}

							}

							}

							AST tmp149_AST = null;

							tmp149_AST = astFactory.create(LT(1));

							astFactory.addASTChild(currentAST, tmp149_AST);

							match(LITERAL_between);

							exp_simple();

							astFactory.addASTChild(currentAST, returnAST);

							AST tmp150_AST = null;

							tmp150_AST = astFactory.create(LT(1));

							astFactory.addASTChild(currentAST, tmp150_AST);

							match(LITERAL_and);

							exp_simple();

							astFactory.addASTChild(currentAST, returnAST);

							logical_factor_AST = (AST)currentAST.root;

						}

						else {

							boolean synPredMatched161 = false;

							if (((_tokenSet_14.member(LA(1))) && (_tokenSet_77.member(LA(2))) && (_tokenSet_78.member(LA(3))) && (_tokenSet_79.member(LA(4))))) {

								int _m161 = mark();

								synPredMatched161 = true;

								inputState.guessing++;

								try {

									{

									exp_simple();

									match(LITERAL_is);

									{

									switch ( LA(1)) {

									case LITERAL_not:

									{

										match(LITERAL_not);

										break;

									}

									case LITERAL_null:

									{

										break;

									}

									default:

									{

										throw new NoViableAltException(LT(1), getFilename());

									}

									}

									}

									match(LITERAL_null);

									}

								}

								catch (RecognitionException pe) {

									synPredMatched161 = false;

								}

								rewind(_m161);

								inputState.guessing--;

							}

							if ( synPredMatched161 ) {

								exp_simple();

								astFactory.addASTChild(currentAST, returnAST);

								AST tmp151_AST = null;

								tmp151_AST = astFactory.create(LT(1));

								astFactory.addASTChild(currentAST, tmp151_AST);

								match(LITERAL_is);

								{

								switch ( LA(1)) {

								case LITERAL_not:

								{

									AST tmp152_AST = null;

									tmp152_AST = astFactory.create(LT(1));

									astFactory.addASTChild(currentAST, tmp152_AST);

									match(LITERAL_not);

									break;

								}

								case LITERAL_null:

								{

									break;

								}

								default:

								{

									throw new NoViableAltException(LT(1), getFilename());

								}

								}

								}

								AST tmp153_AST = null;

								tmp153_AST = astFactory.create(LT(1));

								astFactory.addASTChild(currentAST, tmp153_AST);

								match(LITERAL_null);

								logical_factor_AST = (AST)currentAST.root;

							}

							else {

								boolean synPredMatched164 = false;

								if (((_tokenSet_80.member(LA(1))) && (_tokenSet_81.member(LA(2))) && (_tokenSet_82.member(LA(3))) && (_tokenSet_83.member(LA(4))))) {

									int _m164 = mark();

									synPredMatched164 = true;

									inputState.guessing++;

									try {

										{

										quantified_factor();

										}

									}

									catch (RecognitionException pe) {

										synPredMatched164 = false;

									}

									rewind(_m164);

									inputState.guessing--;

								}

								if ( synPredMatched164 ) {

									quantified_factor();

									astFactory.addASTChild(currentAST, returnAST);

									logical_factor_AST = (AST)currentAST.root;

								}

								else {

									boolean synPredMatched166 = false;

									if (((LA(1)==LITERAL_not) && (_tokenSet_33.member(LA(2))) && (_tokenSet_34.member(LA(3))) && (_tokenSet_35.member(LA(4))))) {

										int _m166 = mark();

										synPredMatched166 = true;

										inputState.guessing++;

										try {

											{

											match(LITERAL_not);

											condition();

											}

										}

										catch (RecognitionException pe) {

											synPredMatched166 = false;

										}

										rewind(_m166);

										inputState.guessing--;

									}

									if ( synPredMatched166 ) {

										AST tmp154_AST = null;

										tmp154_AST = astFactory.create(LT(1));

										astFactory.addASTChild(currentAST, tmp154_AST);

										match(LITERAL_not);

										condition();

										astFactory.addASTChild(currentAST, returnAST);

										logical_factor_AST = (AST)currentAST.root;

									}

									else if ((LA(1)==OPEN_PAREN) && (_tokenSet_33.member(LA(2))) && (_tokenSet_34.member(LA(3))) && (_tokenSet_35.member(LA(4)))) {

										{

										AST tmp155_AST = null;

										tmp155_AST = astFactory.create(LT(1));

										astFactory.addASTChild(currentAST, tmp155_AST);

										match(OPEN_PAREN);

										condition();

										astFactory.addASTChild(currentAST, returnAST);

										AST tmp156_AST = null;

										tmp156_AST = astFactory.create(LT(1));

										astFactory.addASTChild(currentAST, tmp156_AST);

										match(CLOSE_PAREN);

										}

										logical_factor_AST = (AST)currentAST.root;

									}

									else {

										throw new NoViableAltException(LT(1), getFilename());

									}

									}}}}}}

								}

								catch (RecognitionException ex) {

									if (inputState.guessing==0) {

										reportError(ex);

										consume();

										consumeUntil(_tokenSet_36);

									} else {

									  throw ex;

									}

								}

								returnAST = logical_factor_AST;

							}

							

	public final void comparison_op() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST comparison_op_AST = null;

		

		try {      // for error handling

			switch ( LA(1)) {

			case EQ:

			{

				AST tmp157_AST = null;

				tmp157_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp157_AST);

				match(EQ);

				comparison_op_AST = (AST)currentAST.root;

				break;

			}

			case LT:

			{

				AST tmp158_AST = null;

				tmp158_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp158_AST);

				match(LT);

				comparison_op_AST = (AST)currentAST.root;

				break;

			}

			case GT:

			{

				AST tmp159_AST = null;

				tmp159_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp159_AST);

				match(GT);

				comparison_op_AST = (AST)currentAST.root;

				break;

			}

			case NOT_EQ:

			{

				AST tmp160_AST = null;

				tmp160_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp160_AST);

				match(NOT_EQ);

				comparison_op_AST = (AST)currentAST.root;

				break;

			}

			case LE:

			{

				AST tmp161_AST = null;

				tmp161_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp161_AST);

				match(LE);

				comparison_op_AST = (AST)currentAST.root;

				break;

			}

			case GE:

			{

				AST tmp162_AST = null;

				tmp162_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp162_AST);

				match(GE);

				comparison_op_AST = (AST)currentAST.root;

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_84);

			} else {

			  throw ex;

			}

		}

		returnAST = comparison_op_AST;

	}

	

	public final void exp_set() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST exp_set_AST = null;

		

		try {      // for error handling

			boolean synPredMatched180 = false;

			if (((_tokenSet_14.member(LA(1))) && (_tokenSet_85.member(LA(2))) && (_tokenSet_86.member(LA(3))) && (_tokenSet_30.member(LA(4))))) {

				int _m180 = mark();

				synPredMatched180 = true;

				inputState.guessing++;

				try {

					{

					exp_simple();

					}

				}

				catch (RecognitionException pe) {

					synPredMatched180 = false;

				}

				rewind(_m180);

				inputState.guessing--;

			}

			if ( synPredMatched180 ) {

				exp_simple();

				astFactory.addASTChild(currentAST, returnAST);

				exp_set_AST = (AST)currentAST.root;

			}

			else if ((LA(1)==OPEN_PAREN) && (LA(2)==OPEN_PAREN||LA(2)==LITERAL_select) && (_tokenSet_4.member(LA(3))) && (_tokenSet_5.member(LA(4)))) {

				subquery();

				astFactory.addASTChild(currentAST, returnAST);

				exp_set_AST = (AST)currentAST.root;

			}

			else {

				throw new NoViableAltException(LT(1), getFilename());

			}

			

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_36);

			} else {

			  throw ex;

			}

		}

		returnAST = exp_set_AST;

	}

	

	public final void quantified_factor() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST quantified_factor_AST = null;

		

		try {      // for error handling

			boolean synPredMatched171 = false;

			if (((_tokenSet_14.member(LA(1))) && (_tokenSet_65.member(LA(2))) && (_tokenSet_87.member(LA(3))) && (_tokenSet_82.member(LA(4))))) {

				int _m171 = mark();

				synPredMatched171 = true;

				inputState.guessing++;

				try {

					{

					exp_simple();

					comparison_op();

					{

					switch ( LA(1)) {

					case LITERAL_all:

					{

						match(LITERAL_all);

						break;

					}

					case LITERAL_any:

					{

						match(LITERAL_any);

						break;

					}

					case OPEN_PAREN:

					{

						break;

					}

					default:

					{

						throw new NoViableAltException(LT(1), getFilename());

					}

					}

					}

					subquery();

					}

				}

				catch (RecognitionException pe) {

					synPredMatched171 = false;

				}

				rewind(_m171);

				inputState.guessing--;

			}

			if ( synPredMatched171 ) {

				exp_simple();

				astFactory.addASTChild(currentAST, returnAST);

				comparison_op();

				astFactory.addASTChild(currentAST, returnAST);

				{

				switch ( LA(1)) {

				case LITERAL_all:

				{

					AST tmp163_AST = null;

					tmp163_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp163_AST);

					match(LITERAL_all);

					break;

				}

				case LITERAL_any:

				{

					AST tmp164_AST = null;

					tmp164_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp164_AST);

					match(LITERAL_any);

					break;

				}

				case OPEN_PAREN:

				{

					break;

				}

				default:

				{

					throw new NoViableAltException(LT(1), getFilename());

				}

				}

				}

				subquery();

				astFactory.addASTChild(currentAST, returnAST);

				quantified_factor_AST = (AST)currentAST.root;

			}

			else {

				boolean synPredMatched175 = false;

				if (((LA(1)==LITERAL_not||LA(1)==LITERAL_exists))) {

					int _m175 = mark();

					synPredMatched175 = true;

					inputState.guessing++;

					try {

						{

						{

						switch ( LA(1)) {

						case LITERAL_not:

						{

							match(LITERAL_not);

							break;

						}

						case LITERAL_exists:

						{

							break;

						}

						default:

						{

							throw new NoViableAltException(LT(1), getFilename());

						}

						}

						}

						match(LITERAL_exists);

						subquery();

						}

					}

					catch (RecognitionException pe) {

						synPredMatched175 = false;

					}

					rewind(_m175);

					inputState.guessing--;

				}

				if ( synPredMatched175 ) {

					{

					switch ( LA(1)) {

					case LITERAL_not:

					{

						AST tmp165_AST = null;

						tmp165_AST = astFactory.create(LT(1));

						astFactory.addASTChild(currentAST, tmp165_AST);

						match(LITERAL_not);

						break;

					}

					case LITERAL_exists:

					{

						break;

					}

					default:

					{

						throw new NoViableAltException(LT(1), getFilename());

					}

					}

					}

					AST tmp166_AST = null;

					tmp166_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp166_AST);

					match(LITERAL_exists);

					subquery();

					astFactory.addASTChild(currentAST, returnAST);

					quantified_factor_AST = (AST)currentAST.root;

				}

				else if ((LA(1)==OPEN_PAREN) && (LA(2)==OPEN_PAREN||LA(2)==LITERAL_select) && (_tokenSet_4.member(LA(3))) && (_tokenSet_5.member(LA(4)))) {

					subquery();

					astFactory.addASTChild(currentAST, returnAST);

					quantified_factor_AST = (AST)currentAST.root;

				}

				else {

					throw new NoViableAltException(LT(1), getFilename());

				}

				}

			}

			catch (RecognitionException ex) {

				if (inputState.guessing==0) {

					reportError(ex);

					consume();

					consumeUntil(_tokenSet_36);

				} else {

				  throw ex;

				}

			}

			returnAST = quantified_factor_AST;

		}

		

	public final void sorted_def() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST sorted_def_AST = null;

		

		try {      // for error handling

			{

			boolean synPredMatched198 = false;

			if (((_tokenSet_14.member(LA(1))) && (_tokenSet_15.member(LA(2))) && (_tokenSet_88.member(LA(3))) && (_tokenSet_89.member(LA(4))))) {

				int _m198 = mark();

				synPredMatched198 = true;

				inputState.guessing++;

				try {

					{

					expression();

					}

				}

				catch (RecognitionException pe) {

					synPredMatched198 = false;

				}

				rewind(_m198);

				inputState.guessing--;

			}

			if ( synPredMatched198 ) {

				expression();

				astFactory.addASTChild(currentAST, returnAST);

			}

			else {

				boolean synPredMatched200 = false;

				if (((LA(1)==NUMBER) && (_tokenSet_90.member(LA(2))) && (_tokenSet_11.member(LA(3))) && (_tokenSet_89.member(LA(4))))) {

					int _m200 = mark();

					synPredMatched200 = true;

					inputState.guessing++;

					try {

						{

						match(NUMBER);

						}

					}

					catch (RecognitionException pe) {

						synPredMatched200 = false;

					}

					rewind(_m200);

					inputState.guessing--;

				}

				if ( synPredMatched200 ) {

					AST tmp167_AST = null;

					tmp167_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp167_AST);

					match(NUMBER);

				}

				else {

					throw new NoViableAltException(LT(1), getFilename());

				}

				}

				}

				{

				switch ( LA(1)) {

				case LITERAL_asc:

				{

					AST tmp168_AST = null;

					tmp168_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp168_AST);

					match(LITERAL_asc);

					break;

				}

				case LITERAL_desc:

				{

					AST tmp169_AST = null;

					tmp169_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp169_AST);

					match(LITERAL_desc);

					break;

				}

				case EOF:

				case SEMI:

				case LITERAL_union:

				case OPEN_PAREN:

				case CLOSE_PAREN:

				case LITERAL_select:

				case COMMA:

				case LITERAL_order:

				case LITERAL_for:

				case LITERAL_update:

				case LITERAL_delete:

				{

					break;

				}

				default:

				{

					throw new NoViableAltException(LT(1), getFilename());

				}

				}

				}

				sorted_def_AST = (AST)currentAST.root;

			}

			catch (RecognitionException ex) {

				if (inputState.guessing==0) {

					reportError(ex);

					consume();

					consumeUntil(_tokenSet_91);

				} else {

				  throw ex;

				}

			}

			returnAST = sorted_def_AST;

		}

		

	public final void subquery_update() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST subquery_update_AST = null;

		

		try {      // for error handling

			AST tmp170_AST = null;

			tmp170_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp170_AST);

			match(LITERAL_update);

			table_alias();

			astFactory.addASTChild(currentAST, returnAST);

			AST tmp171_AST = null;

			tmp171_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp171_AST);

			match(LITERAL_set);

			AST tmp172_AST = null;

			tmp172_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp172_AST);

			match(OPEN_PAREN);

			column_spec();

			astFactory.addASTChild(currentAST, returnAST);

			{

			_loop224:

			do {

				if ((LA(1)==COMMA)) {

					AST tmp173_AST = null;

					tmp173_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp173_AST);

					match(COMMA);

					column_spec();

					astFactory.addASTChild(currentAST, returnAST);

				}

				else {

					break _loop224;

				}

				

			} while (true);

			}

			AST tmp174_AST = null;

			tmp174_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp174_AST);

			match(CLOSE_PAREN);

			AST tmp175_AST = null;

			tmp175_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp175_AST);

			match(EQ);

			subquery();

			astFactory.addASTChild(currentAST, returnAST);

			AST tmp176_AST = null;

			tmp176_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp176_AST);

			match(LITERAL_where);

			condition();

			astFactory.addASTChild(currentAST, returnAST);

			subquery_update_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_3);

			} else {

			  throw ex;

			}

		}

		returnAST = subquery_update_AST;

	}

	

	public final void simple_update() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST simple_update_AST = null;

		

		try {      // for error handling

			AST tmp177_AST = null;

			tmp177_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp177_AST);

			match(LITERAL_update);

			table_alias();

			astFactory.addASTChild(currentAST, returnAST);

			AST tmp178_AST = null;

			tmp178_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp178_AST);

			match(LITERAL_set);

			column_spec();

			astFactory.addASTChild(currentAST, returnAST);

			AST tmp179_AST = null;

			tmp179_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp179_AST);

			match(EQ);

			{

			boolean synPredMatched216 = false;

			if (((_tokenSet_14.member(LA(1))) && (_tokenSet_92.member(LA(2))) && (_tokenSet_93.member(LA(3))) && (_tokenSet_94.member(LA(4))))) {

				int _m216 = mark();

				synPredMatched216 = true;

				inputState.guessing++;

				try {

					{

					expression();

					}

				}

				catch (RecognitionException pe) {

					synPredMatched216 = false;

				}

				rewind(_m216);

				inputState.guessing--;

			}

			if ( synPredMatched216 ) {

				expression();

				astFactory.addASTChild(currentAST, returnAST);

			}

			else if ((LA(1)==OPEN_PAREN) && (LA(2)==OPEN_PAREN||LA(2)==LITERAL_select) && (_tokenSet_4.member(LA(3))) && (_tokenSet_5.member(LA(4)))) {

				subquery();

				astFactory.addASTChild(currentAST, returnAST);

			}

			else if ((LA(1)==LITERAL_where||LA(1)==COMMA)) {

			}

			else {

				throw new NoViableAltException(LT(1), getFilename());

			}

			

			}

			{

			_loop221:

			do {

				if ((LA(1)==COMMA)) {

					AST tmp180_AST = null;

					tmp180_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp180_AST);

					match(COMMA);

					column_spec();

					astFactory.addASTChild(currentAST, returnAST);

					AST tmp181_AST = null;

					tmp181_AST = astFactory.create(LT(1));

					astFactory.addASTChild(currentAST, tmp181_AST);

					match(EQ);

					{

					boolean synPredMatched220 = false;

					if (((_tokenSet_14.member(LA(1))) && (_tokenSet_92.member(LA(2))) && (_tokenSet_93.member(LA(3))) && (_tokenSet_94.member(LA(4))))) {

						int _m220 = mark();

						synPredMatched220 = true;

						inputState.guessing++;

						try {

							{

							expression();

							}

						}

						catch (RecognitionException pe) {

							synPredMatched220 = false;

						}

						rewind(_m220);

						inputState.guessing--;

					}

					if ( synPredMatched220 ) {

						expression();

						astFactory.addASTChild(currentAST, returnAST);

					}

					else if ((LA(1)==OPEN_PAREN) && (LA(2)==OPEN_PAREN||LA(2)==LITERAL_select) && (_tokenSet_4.member(LA(3))) && (_tokenSet_5.member(LA(4)))) {

						subquery();

						astFactory.addASTChild(currentAST, returnAST);

					}

					else {

						throw new NoViableAltException(LT(1), getFilename());

					}

					

					}

				}

				else {

					break _loop221;

				}

				

			} while (true);

			}

			AST tmp182_AST = null;

			tmp182_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp182_AST);

			match(LITERAL_where);

			condition();

			astFactory.addASTChild(currentAST, returnAST);

			simple_update_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_3);

			} else {

			  throw ex;

			}

		}

		returnAST = simple_update_AST;

	}

	

	public final void keyword() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST keyword_AST = null;

		

		try {      // for error handling

			switch ( LA(1)) {

			case LITERAL_abs:

			{

				AST tmp183_AST = null;

				tmp183_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp183_AST);

				match(LITERAL_abs);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_ascii:

			{

				AST tmp184_AST = null;

				tmp184_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp184_AST);

				match(LITERAL_ascii);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_ceil:

			{

				AST tmp185_AST = null;

				tmp185_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp185_AST);

				match(LITERAL_ceil);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_chartorowid:

			{

				AST tmp186_AST = null;

				tmp186_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp186_AST);

				match(LITERAL_chartorowid);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_chr:

			{

				AST tmp187_AST = null;

				tmp187_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp187_AST);

				match(LITERAL_chr);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_concat:

			{

				AST tmp188_AST = null;

				tmp188_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp188_AST);

				match(LITERAL_concat);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_convert:

			{

				AST tmp189_AST = null;

				tmp189_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp189_AST);

				match(LITERAL_convert);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_count:

			{

				AST tmp190_AST = null;

				tmp190_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp190_AST);

				match(LITERAL_count);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_decode:

			{

				AST tmp191_AST = null;

				tmp191_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp191_AST);

				match(LITERAL_decode);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_dump:

			{

				AST tmp192_AST = null;

				tmp192_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp192_AST);

				match(LITERAL_dump);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_floor:

			{

				AST tmp193_AST = null;

				tmp193_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp193_AST);

				match(LITERAL_floor);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_greatest:

			{

				AST tmp194_AST = null;

				tmp194_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp194_AST);

				match(LITERAL_greatest);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_hextoraw:

			{

				AST tmp195_AST = null;

				tmp195_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp195_AST);

				match(LITERAL_hextoraw);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_initcap:

			{

				AST tmp196_AST = null;

				tmp196_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp196_AST);

				match(LITERAL_initcap);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_instr:

			{

				AST tmp197_AST = null;

				tmp197_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp197_AST);

				match(LITERAL_instr);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_intersect:

			{

				AST tmp198_AST = null;

				tmp198_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp198_AST);

				match(LITERAL_intersect);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_least:

			{

				AST tmp199_AST = null;

				tmp199_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp199_AST);

				match(LITERAL_least);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_length:

			{

				AST tmp200_AST = null;

				tmp200_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp200_AST);

				match(LITERAL_length);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_lower:

			{

				AST tmp201_AST = null;

				tmp201_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp201_AST);

				match(LITERAL_lower);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_lpad:

			{

				AST tmp202_AST = null;

				tmp202_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp202_AST);

				match(LITERAL_lpad);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_ltrim:

			{

				AST tmp203_AST = null;

				tmp203_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp203_AST);

				match(LITERAL_ltrim);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_nvl:

			{

				AST tmp204_AST = null;

				tmp204_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp204_AST);

				match(LITERAL_nvl);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_power:

			{

				AST tmp205_AST = null;

				tmp205_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp205_AST);

				match(LITERAL_power);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_rawtohex:

			{

				AST tmp206_AST = null;

				tmp206_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp206_AST);

				match(LITERAL_rawtohex);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_replace:

			{

				AST tmp207_AST = null;

				tmp207_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp207_AST);

				match(LITERAL_replace);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_round:

			{

				AST tmp208_AST = null;

				tmp208_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp208_AST);

				match(LITERAL_round);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_rowidtochar:

			{

				AST tmp209_AST = null;

				tmp209_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp209_AST);

				match(LITERAL_rowidtochar);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_rpad:

			{

				AST tmp210_AST = null;

				tmp210_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp210_AST);

				match(LITERAL_rpad);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_rtrim:

			{

				AST tmp211_AST = null;

				tmp211_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp211_AST);

				match(LITERAL_rtrim);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_sign:

			{

				AST tmp212_AST = null;

				tmp212_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp212_AST);

				match(LITERAL_sign);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_soundex:

			{

				AST tmp213_AST = null;

				tmp213_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp213_AST);

				match(LITERAL_soundex);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_sqrt:

			{

				AST tmp214_AST = null;

				tmp214_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp214_AST);

				match(LITERAL_sqrt);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_substr:

			{

				AST tmp215_AST = null;

				tmp215_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp215_AST);

				match(LITERAL_substr);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_sysdate:

			{

				AST tmp216_AST = null;

				tmp216_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp216_AST);

				match(LITERAL_sysdate);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_to_char:

			{

				AST tmp217_AST = null;

				tmp217_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp217_AST);

				match(LITERAL_to_char);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_to_date:

			{

				AST tmp218_AST = null;

				tmp218_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp218_AST);

				match(LITERAL_to_date);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_to_number:

			{

				AST tmp219_AST = null;

				tmp219_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp219_AST);

				match(LITERAL_to_number);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_translate:

			{

				AST tmp220_AST = null;

				tmp220_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp220_AST);

				match(LITERAL_translate);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_trunc:

			{

				AST tmp221_AST = null;

				tmp221_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp221_AST);

				match(LITERAL_trunc);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_upper:

			{

				AST tmp222_AST = null;

				tmp222_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp222_AST);

				match(LITERAL_upper);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_user:

			{

				AST tmp223_AST = null;

				tmp223_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp223_AST);

				match(LITERAL_user);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_userenv:

			{

				AST tmp224_AST = null;

				tmp224_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp224_AST);

				match(LITERAL_userenv);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			case LITERAL_vsize:

			{

				AST tmp225_AST = null;

				tmp225_AST = astFactory.create(LT(1));

				astFactory.addASTChild(currentAST, tmp225_AST);

				match(LITERAL_vsize);

				keyword_AST = (AST)currentAST.root;

				break;

			}

			default:

			{

				throw new NoViableAltException(LT(1), getFilename());

			}

			}

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_41);

			} else {

			  throw ex;

			}

		}

		returnAST = keyword_AST;

	}

	

	public final void quoted_string() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST quoted_string_AST = null;

		

		try {      // for error handling

			AST tmp226_AST = null;

			tmp226_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp226_AST);

			match(QUOTED_STRING);

			quoted_string_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_1);

			} else {

			  throw ex;

			}

		}

		returnAST = quoted_string_AST;

	}

	

	public final void match_string() throws RecognitionException, TokenStreamException {

		

		returnAST = null;

		ASTPair currentAST = new ASTPair();

		AST match_string_AST = null;

		

		try {      // for error handling

			AST tmp227_AST = null;

			tmp227_AST = astFactory.create(LT(1));

			astFactory.addASTChild(currentAST, tmp227_AST);

			match(QUOTED_STRING);

			match_string_AST = (AST)currentAST.root;

		}

		catch (RecognitionException ex) {

			if (inputState.guessing==0) {

				reportError(ex);

				consume();

				consumeUntil(_tokenSet_1);

			} else {

			  throw ex;

			}

		}

		returnAST = match_string_AST;

	}

	

	

	public static final String[] _tokenNames = {

		"<0>",

		"EOF",

		"<2>",

		"NULL_TREE_LOOKAHEAD",

		"SQL_STATEMENT",

		"SELECT_LIST",

		"TABLE_REFERENCE_LIST",

		"WHERE_CONDITION",

		"SUBQUERY",

		"SQL_IDENTIFIER",

		"SQL_LITERAL",

		"FUNCTION",

		"GROUP_FUNCTION",

		"USER_FUNCTION",

		"MULTIPLY",

		"SEMI",

		"\"union\"",

		"OPEN_PAREN",

		"CLOSE_PAREN",

		"\"select\"",

		"\"all\"",

		"\"distinct\"",

		"\"from\"",

		"\"where\"",

		"COMMA",

		"ASTERISK",

		"DOT",

		"PLUS",

		"MINUS",

		"\"as\"",

		"DIVIDE",

		"VERTBAR",

		"NUMBER",

		"QUOTED_STRING",

		"\"null\"",

		"QUESTION_MARK",

		"BIND_NAME",

		"\"abs\"",

		"\"ceil\"",

		"\"floor\"",

		"\"mod\"",

		"\"power\"",

		"\"round\"",

		"\"sign\"",

		"\"sqrt\"",

		"\"trunc\"",

		"\"chr\"",

		"\"initcap\"",

		"\"lower\"",

		"\"lpad\"",

		"\"ltrim\"",

		"\"replace\"",

		"\"rpad\"",

		"\"rtrim\"",

		"\"soundex\"",

		"\"substr\"",

		"\"translate\"",

		"\"upper\"",

		"\"ascii\"",

		"\"instr\"",

		"\"length\"",

		"\"concat\"",

		"\"avg\"",

		"\"count\"",

		"\"max\"",

		"\"min\"",

		"\"stddev\"",

		"\"sum\"",

		"\"variance\"",

		"\"chartorowid\"",

		"\"convert\"",

		"\"hextoraw\"",

		"\"rawtohex\"",

		"\"rowidtochar\"",

		"\"to_char\"",

		"\"to_date\"",

		"\"to_number\"",

		"\"decode\"",

		"\"dump\"",

		"\"greatest\"",

		"\"least\"",

		"\"nvl\"",

		"\"uid\"",

		"\"userenv\"",

		"\"vsize\"",

		"\"user\"",

		"\"sysdate\"",

		"\"?\"",

		"AT_SIGN",

		"\"or\"",

		"\"and\"",

		"\"prior\"",

		"\"not\"",

		"\"in\"",

		"\"like\"",

		"\"escape\"",

		"\"between\"",

		"\"is\"",

		"\"any\"",

		"\"exists\"",

		"EQ",

		"LT",

		"GT",

		"NOT_EQ",

		"LE",

		"GE",

		"\"start\"",

		"\"with\"",

		"\"connect\"",

		"\"by\"",

		"\"group\"",

		"\"having\"",

		"\"intersect\"",

		"\"minus\"",

		"\"order\"",

		"\"asc\"",

		"\"desc\"",

		"\"for\"",

		"\"update\"",

		"\"of\"",

		"\"nowait\"",

		"\"delete\"",

		"\"set\"",

		"IDENTIFIER",

		"N",

		"BIND",

		"DOUBLE_QUOTE",

		"WS",

		"ML_COMMENT"

	};

	

	protected void buildTokenTypeASTClassMap() {

		tokenTypeToASTClassMap=null;

	};

	

	private static final long[] mk_tokenSet_0() {

		long[] data = { 655360L, 162129586585337856L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

	private static final long[] mk_tokenSet_1() {

		long[] data = { 2L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());

	private static final long[] mk_tokenSet_2() {

		long[] data = { 655362L, 162129586585337856L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());

	private static final long[] mk_tokenSet_3() {

		long[] data = { 688130L, 162129586585337856L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());

	private static final long[] mk_tokenSet_4() {

		long[] data = { -3854958592L, 576742227288522751L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());

	private static final long[] mk_tokenSet_5() {

		long[] data = { -8781824L, 576742227288522751L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());

	private static final long[] mk_tokenSet_6() {

		long[] data = { 1015810L, 172262685746921472L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());

	private static final long[] mk_tokenSet_7() {

		long[] data = { -4611687246788034560L, 576742227288260576L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());

	private static final long[] mk_tokenSet_8() {

		long[] data = { -4611687246184054784L, 864972603456749536L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());

	private static final long[] mk_tokenSet_9() {

		long[] data = { -4611687246787903488L, 864972603439972320L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());

	private static final long[] mk_tokenSet_10() {

		long[] data = { -4611687246788034560L, 864972603439972320L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());

	private static final long[] mk_tokenSet_11() {

		long[] data = { -3288367102L, 749695406438350847L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());

	private static final long[] mk_tokenSet_12() {

		long[] data = { -32766L, 1146021004519145471L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());

	private static final long[] mk_tokenSet_13() {

		long[] data = { -32766L, 1152780747782750207L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());

	private static final long[] mk_tokenSet_14() {

		long[] data = { -3892183040L, 576742227288522751L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());

	private static final long[] mk_tokenSet_15() {

		long[] data = { -552632318L, 755760312476499967L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());

	private static final long[] mk_tokenSet_16() {

		long[] data = { 1015810L, 280349076803813376L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());

	private static final long[] mk_tokenSet_17() {

		long[] data = { 4194304L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());

	private static final long[] mk_tokenSet_18() {

		long[] data = { 9404418L, 173199469653786624L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());

	private static final long[] mk_tokenSet_19() {

		long[] data = { 1015810L, 173199469653786624L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());

	private static final long[] mk_tokenSet_20() {

		long[] data = { 1015810L, 173177479421231104L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());

	private static final long[] mk_tokenSet_21() {

		long[] data = { 1015810L, 173107110677053440L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());

	private static final long[] mk_tokenSet_22() {

		long[] data = { -4611687246754480128L, 576742227288260576L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());

	private static final long[] mk_tokenSet_23() {

		long[] data = { -12451840L, 576742227288522751L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());

	private static final long[] mk_tokenSet_24() {

		long[] data = { -9043968L, 576742227288522751L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_24 = new BitSet(mk_tokenSet_24());

	private static final long[] mk_tokenSet_25() {

		long[] data = { -32766L, 749660221982375935L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_25 = new BitSet(mk_tokenSet_25());

	private static final long[] mk_tokenSet_26() {

		long[] data = { 20971520L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_26 = new BitSet(mk_tokenSet_26());

	private static final long[] mk_tokenSet_27() {

		long[] data = { -4611687246251163648L, 576742227288260576L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_27 = new BitSet(mk_tokenSet_27());

	private static final long[] mk_tokenSet_28() {

		long[] data = { -4611687246761852926L, 749660221965336544L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_28 = new BitSet(mk_tokenSet_28());

	private static final long[] mk_tokenSet_29() {

		long[] data = { -3288367102L, 749704237293764607L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_29 = new BitSet(mk_tokenSet_29());

	private static final long[] mk_tokenSet_30() {

		long[] data = { -32766L, 1146025348341694463L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_30 = new BitSet(mk_tokenSet_30());

	private static final long[] mk_tokenSet_31() {

		long[] data = { 26181634L, 173199469653786624L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_31 = new BitSet(mk_tokenSet_31());

	private static final long[] mk_tokenSet_32() {

		long[] data = { -32766L, 1152921502450974719L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_32 = new BitSet(mk_tokenSet_32());

	private static final long[] mk_tokenSet_33() {

		long[] data = { -3892183040L, 576742262050914303L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_33 = new BitSet(mk_tokenSet_33());

	private static final long[] mk_tokenSet_34() {

		long[] data = { -569769984L, 576746605873463295L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_34 = new BitSet(mk_tokenSet_34());

	private static final long[] mk_tokenSet_35() {

		long[] data = { -549584896L, 576746623053332479L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_35 = new BitSet(mk_tokenSet_35());

	private static final long[] mk_tokenSet_36() {

		long[] data = { 1015810L, 173199469754449920L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_36 = new BitSet(mk_tokenSet_36());

	private static final long[] mk_tokenSet_37() {

		long[] data = { 67108864L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_37 = new BitSet(mk_tokenSet_37());

	private static final long[] mk_tokenSet_38() {

		long[] data = { -4611687246157873150L, 1037890598133825504L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_38 = new BitSet(mk_tokenSet_38());

	private static final long[] mk_tokenSet_39() {

		long[] data = { -4611687246229176318L, 749664566156984288L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_39 = new BitSet(mk_tokenSet_39());

	private static final long[] mk_tokenSet_40() {

		long[] data = { 30375938L, 461429845805498368L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_40 = new BitSet(mk_tokenSet_40());

	private static final long[] mk_tokenSet_41() {

		long[] data = { -4611687242496245758L, 1116848675440295904L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_41 = new BitSet(mk_tokenSet_41());

	private static final long[] mk_tokenSet_42() {

		long[] data = { -3178494L, 756560705234141183L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_42 = new BitSet(mk_tokenSet_42());

	private static final long[] mk_tokenSet_43() {

		long[] data = { -32766L, 756604737373077503L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_43 = new BitSet(mk_tokenSet_43());

	private static final long[] mk_tokenSet_44() {

		long[] data = { -4611687242563354622L, 756560705233879008L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_44 = new BitSet(mk_tokenSet_44());

	private static final long[] mk_tokenSet_45() {

		long[] data = { -67141630L, 756604737373077503L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_45 = new BitSet(mk_tokenSet_45());

	private static final long[] mk_tokenSet_46() {

		long[] data = { -32766L, 1152921504598458367L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_46 = new BitSet(mk_tokenSet_46());

	private static final long[] mk_tokenSet_47() {

		long[] data = { 4611685880988434432L, 2097120L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_47 = new BitSet(mk_tokenSet_47());

	private static final long[] mk_tokenSet_48() {

		long[] data = { -553254912L, 576742227288522751L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_48 = new BitSet(mk_tokenSet_48());

	private static final long[] mk_tokenSet_49() {

		long[] data = { -3855220736L, 576742227288522751L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_49 = new BitSet(mk_tokenSet_49());

	private static final long[] mk_tokenSet_50() {

		long[] data = { -570032128L, 576742227288522751L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_50 = new BitSet(mk_tokenSet_50());

	private static final long[] mk_tokenSet_51() {

		long[] data = { -32766L, 756560705234141183L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_51 = new BitSet(mk_tokenSet_51());

	private static final long[] mk_tokenSet_52() {

		long[] data = { -4611687242496245758L, 756560705233879008L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_52 = new BitSet(mk_tokenSet_52());

	private static final long[] mk_tokenSet_53() {

		long[] data = { -553517056L, 576742227288522751L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_53 = new BitSet(mk_tokenSet_53());

	private static final long[] mk_tokenSet_54() {

		long[] data = { -550109184L, 576742227288522751L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_54 = new BitSet(mk_tokenSet_54());

	private static final long[] mk_tokenSet_55() {

		long[] data = { 131072L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_55 = new BitSet(mk_tokenSet_55());

	private static final long[] mk_tokenSet_56() {

		long[] data = { -4611687246653816832L, 576742227288260576L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_56 = new BitSet(mk_tokenSet_56());

	private static final long[] mk_tokenSet_57() {

		long[] data = { -4611687242563354622L, 828618299271806944L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_57 = new BitSet(mk_tokenSet_57());

	private static final long[] mk_tokenSet_58() {

		long[] data = { -4611687246224982014L, 749660221982113760L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_58 = new BitSet(mk_tokenSet_58());

	private static final long[] mk_tokenSet_59() {

		long[] data = { -4611687246224982014L, 749660221965336544L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_59 = new BitSet(mk_tokenSet_59());

	private static final long[] mk_tokenSet_60() {

		long[] data = { -4611687246224982014L, 749660222065999840L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_60 = new BitSet(mk_tokenSet_60());

	private static final long[] mk_tokenSet_61() {

		long[] data = { -4611687246224982014L, 1037890598117048288L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_61 = new BitSet(mk_tokenSet_61());

	private static final long[] mk_tokenSet_62() {

		long[] data = { -4611687246242086910L, 1027102190042087392L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_62 = new BitSet(mk_tokenSet_62());

	private static final long[] mk_tokenSet_63() {

		long[] data = { 9076738L, 450359962737049600L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_63 = new BitSet(mk_tokenSet_63());

	private static final long[] mk_tokenSet_64() {

		long[] data = { -3892183040L, 576742227422740479L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_64 = new BitSet(mk_tokenSet_64());

	private static final long[] mk_tokenSet_65() {

		long[] data = { -570294272L, 576746556615557119L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_65 = new BitSet(mk_tokenSet_65());

	private static final long[] mk_tokenSet_66() {

		long[] data = { -550109184L, 576746556749774847L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_66 = new BitSet(mk_tokenSet_66());

	private static final long[] mk_tokenSet_67() {

		long[] data = { -549486590L, 749664551527514111L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_67 = new BitSet(mk_tokenSet_67());

	private static final long[] mk_tokenSet_68() {

		long[] data = { -570294272L, 576742228093829119L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_68 = new BitSet(mk_tokenSet_68());

	private static final long[] mk_tokenSet_69() {

		long[] data = { -550109184L, 576742228093829119L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_69 = new BitSet(mk_tokenSet_69());

	private static final long[] mk_tokenSet_70() {

		long[] data = { -549486590L, 749660222871568383L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_70 = new BitSet(mk_tokenSet_70());

	private static final long[] mk_tokenSet_71() {

		long[] data = { -570294272L, 576742228630700031L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_71 = new BitSet(mk_tokenSet_71());

	private static final long[] mk_tokenSet_72() {

		long[] data = { -550109184L, 576742228630700031L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_72 = new BitSet(mk_tokenSet_72());

	private static final long[] mk_tokenSet_73() {

		long[] data = { -549486590L, 749660225555922943L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_73 = new BitSet(mk_tokenSet_73());

	private static final long[] mk_tokenSet_74() {

		long[] data = { -570294272L, 576742231851925503L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_74 = new BitSet(mk_tokenSet_74());

	private static final long[] mk_tokenSet_75() {

		long[] data = { -550109184L, 576742231851925503L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_75 = new BitSet(mk_tokenSet_75());

	private static final long[] mk_tokenSet_76() {

		long[] data = { -550109184L, 576742231919034367L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_76 = new BitSet(mk_tokenSet_76());

	private static final long[] mk_tokenSet_77() {

		long[] data = { -570294272L, 576742235878457343L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_77 = new BitSet(mk_tokenSet_77());

	private static final long[] mk_tokenSet_78() {

		long[] data = { -550109184L, 576742236146892799L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_78 = new BitSet(mk_tokenSet_78());

	private static final long[] mk_tokenSet_79() {

		long[] data = { -549486590L, 749660230924632063L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_79 = new BitSet(mk_tokenSet_79());

	private static final long[] mk_tokenSet_80() {

		long[] data = { -3892183040L, 576742261916696575L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_80 = new BitSet(mk_tokenSet_80());

	private static final long[] mk_tokenSet_81() {

		long[] data = { -569769984L, 576746590975295487L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_81 = new BitSet(mk_tokenSet_81());

	private static final long[] mk_tokenSet_82() {

		long[] data = { -549584896L, 576746573795426303L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_82 = new BitSet(mk_tokenSet_82());

	private static final long[] mk_tokenSet_83() {

		long[] data = { -8519680L, 576746573795426303L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_83 = new BitSet(mk_tokenSet_83());

	private static final long[] mk_tokenSet_84() {

		long[] data = { -3891134464L, 576742244602609663L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_84 = new BitSet(mk_tokenSet_84());

	private static final long[] mk_tokenSet_85() {

		long[] data = { -569409534L, 749660222066262015L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_85 = new BitSet(mk_tokenSet_85());

	private static final long[] mk_tokenSet_86() {

		long[] data = { -32766L, 749704237293764607L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_86 = new BitSet(mk_tokenSet_86());

	private static final long[] mk_tokenSet_87() {

		long[] data = { -550109184L, 576746573795426303L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_87 = new BitSet(mk_tokenSet_87());

	private static final long[] mk_tokenSet_88() {

		long[] data = { -32766L, 756450805879406591L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_88 = new BitSet(mk_tokenSet_88());

	private static final long[] mk_tokenSet_89() {

		long[] data = { -32766L, 1152776403960201215L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_89 = new BitSet(mk_tokenSet_89());

	private static final long[] mk_tokenSet_90() {

		long[] data = { 17793026L, 179018085187977216L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_90 = new BitSet(mk_tokenSet_90());

	private static final long[] mk_tokenSet_91() {

		long[] data = { 17793026L, 172262685746921472L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_91 = new BitSet(mk_tokenSet_91());

	private static final long[] mk_tokenSet_92() {

		long[] data = { -545128448L, 576742227288522751L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_92 = new BitSet(mk_tokenSet_92());

	private static final long[] mk_tokenSet_93() {

		long[] data = { -541720576L, 576742262050914303L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_93 = new BitSet(mk_tokenSet_93());

	private static final long[] mk_tokenSet_94() {

		long[] data = { -541196288L, 576746605873463295L, 0L, 0L};

		return data;

	}

	public static final BitSet _tokenSet_94 = new BitSet(mk_tokenSet_94());

	

	}

