

function initializeCoreMod() {
    return {
        'itementitydamage': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.item.ItemEntity',
                'methodName': 'func_70097_a', // getDrops
                'methodDesc': '(Lnet/minecraft/util/DamageSource;F)Z'
            },
            'transformer': function(method) {
              print('[HarvestDropsEvent]: Patching Minecraft\' ItemEntity#attackEntityFrom');

                var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
                var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');

                var instructions = method.instructions;
                var lastInstruction = instructions.get(35);

                var newInstructions = new InsnList();

                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                newInstructions.add(new VarInsnNode(Opcodes.FLOAD, 2));


                newInstructions.add(ASM.buildMethodCall(
                    "com/tfar/additionalevents/event/Hooks",
                    "onEntityItemDamage",
                    "(Lnet/minecraft/entity/item/ItemEntity;Lnet/minecraft/util/DamageSource;F)F",
                    ASM.MethodType.STATIC
                ));

                                newInstructions.add(new VarInsnNode(Opcodes.FSTORE, 2));

                method.instructions.insertBefore(lastInstruction, newInstructions);

                return method;
            }
        }
     }
  }