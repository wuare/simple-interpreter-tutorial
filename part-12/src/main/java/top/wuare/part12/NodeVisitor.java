package top.wuare.part12;

import top.wuare.part12.ast.AST;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author wuare
 * @date 2021/7/8
 */
public class NodeVisitor {

    public Object visit(AST node) {

        try {
            String methodName = "visit" + node.getClass().getSimpleName();
            Method method = this.getClass().getMethod(methodName, node.getClass());
            return method.invoke(this, node);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
