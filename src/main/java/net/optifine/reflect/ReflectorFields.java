package net.optifine.reflect;

public class ReflectorFields
{
    private final ReflectorClass reflectorClass;
    private ReflectorField[] reflectorFields;

    public ReflectorFields(ReflectorClass reflectorClass, Class fieldType, int fieldCount)
    {
        this.reflectorClass = reflectorClass;

        if (reflectorClass.exists())
        {
            if (fieldType != null)
            {
                this.reflectorFields = new ReflectorField[fieldCount];

                for (int i = 0; i < this.reflectorFields.length; ++i)
                {
                    this.reflectorFields[i] = new ReflectorField(reflectorClass, fieldType, i);
                }
            }
        }
    }

    public ReflectorClass getReflectorClass()
    {
        return this.reflectorClass;
    }

    public int getFieldCount()
    {
        return 0;
    }

    public ReflectorField getReflectorField(int index)
    {
        return index >= 0 && index < this.reflectorFields.length ? this.reflectorFields[index] : null;
    }
}
