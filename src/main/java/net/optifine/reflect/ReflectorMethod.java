package net.optifine.reflect;

import net.optifine.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectorMethod implements IResolvable
{
    private final ReflectorClass reflectorClass;
    private final String targetMethodName;
    private final Class[] targetMethodParameterTypes;
    private boolean checked;
    private Method targetMethod;

    public ReflectorMethod(ReflectorClass reflectorClass, String targetMethodName)
    {
        this(reflectorClass, targetMethodName, null);
    }

    public ReflectorMethod(ReflectorClass reflectorClass, String targetMethodName, Class[] targetMethodParameterTypes)
    {
        this.checked = false;
        this.targetMethod = null;
        this.reflectorClass = reflectorClass;
        this.targetMethodName = targetMethodName;
        this.targetMethodParameterTypes = targetMethodParameterTypes;
        ReflectorResolver.register(this);
    }

    public Method getTargetMethod()
    {
        if (this.checked)
        {
            return this.targetMethod;
        }
        else
        {
            this.checked = true;
            Class oclass = this.reflectorClass.getTargetClass();

            if (oclass == null)
            {
                return null;
            }
            else
            {
                try
                {
                    if (this.targetMethodParameterTypes == null)
                    {
                        Method[] amethod = getMethods(oclass, this.targetMethodName);

                        if (amethod.length <= 0)
                        {
                            Log.log("(Reflector) Method not present: " + oclass.getName() + "." + this.targetMethodName);
                            return null;
                        }

                        if (amethod.length > 1)
                        {
                            Log.warn("(Reflector) More than one method found: " + oclass.getName() + "." + this.targetMethodName);

                            for (Method method : amethod) {
                                Log.warn("(Reflector)  - " + method);
                            }

                            return null;
                        }

                        this.targetMethod = amethod[0];
                    }
                    else
                    {
                        this.targetMethod = getMethod(oclass, this.targetMethodName, this.targetMethodParameterTypes);
                    }

                    if (this.targetMethod == null)
                    {
                        Log.log("(Reflector) Method not present: " + oclass.getName() + "." + this.targetMethodName);
                        return null;
                    }
                    else
                    {
                        this.targetMethod.setAccessible(true);
                        return this.targetMethod;
                    }
                }
                catch (Throwable throwable)
                {
                    throwable.printStackTrace();
                    return null;
                }
            }
        }
    }

    public boolean exists()
    {
        return this.checked ? this.targetMethod != null : this.getTargetMethod() != null;
    }

    public Class getReturnType()
    {
        Method method = this.getTargetMethod();
        return method == null ? null : method.getReturnType();
    }

    public void deactivate()
    {
        this.checked = true;
        this.targetMethod = null;
    }

    public Object call(Object... params)
    {
        return Reflector.call(this, params);
    }

    public boolean callBoolean(Object... params)
    {
        return Reflector.callBoolean(this, params);
    }

    public int callInt(Object... params)
    {
        return Reflector.callInt(this, params);
    }

    public String callString(Object... params)
    {
        return Reflector.callString(this, params);
    }

    public Object call(Object param)
    {
        return Reflector.call(this, param);
    }

    public boolean callBoolean(Object param)
    {
        return Reflector.callBoolean(this, param);
    }

    public int callInt(Object param)
    {
        return Reflector.callInt(this, param);
    }

    public void callVoid(Object... params)
    {
        Reflector.callVoid(this, params);
    }

    public static Method getMethod(Class cls, String methodName, Class[] paramTypes)
    {
        Method[] amethod = cls.getDeclaredMethods();

        for (Method method : amethod) {
            if (method.getName().equals(methodName)) {
                Class[] aclass = method.getParameterTypes();

                if (Reflector.matchesTypes(paramTypes, aclass)) {
                    return method;
                }
            }
        }

        return null;
    }

    public static Method[] getMethods(Class cls, String methodName)
    {
        List list = new ArrayList();
        Method[] amethod = cls.getDeclaredMethods();

        for (Method method : amethod) {
            if (method.getName().equals(methodName)) {
                list.add(method);
            }
        }

        return (Method[]) list.toArray(new Method[0]);
    }

    public void resolve()
    {
    }
}
