package darks.orm.core.data.tags.express;

import java.util.LinkedList;

/**
 * 逆波兰式运算符接口
 * @author lihua.llh
 *
 */
public interface RPNOper
{

    /**
     * 计算运算符结果（结果需入栈）
     * @param stack 操作数栈
     * @param param 参数
     * @throws ExpressException
     */
	public void compute(LinkedList<Object> stack, Object param) throws ExpressException;
}
