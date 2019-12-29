

function initializeCoreMod() {
    return {
        'droploot': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.block.BlockState',
                'methodName': 'func_215685_b', // getCollisionShape
                'methodDesc': '(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/shapes/ISelectionContext;)Lnet/minecraft/util/math/shapes/VoxelShape;'
            },
            'transformer': function(method) {
              print('[HarvestDropsEvent]: Patching Minecraft\' BlockState#getCollisionShape');

                var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
                var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');

                var instructions = method.instructions;
                var lastInstruction = instructions.get(0);

                var newInstructions = new InsnList();

                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 3));

                newInstructions.add(ASM.buildMethodCall(
                    "com/tfar/additionalevents/event/Hooks",
                    "fireCollisionShapeEvent",
                    "(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/shapes/ISelectionContext;)Lnet/minecraft/util/math/shapes/VoxelShape;",
                    ASM.MethodType.STATIC
                ));

                                newInstructions.add(new InsnNode(Opcodes.ARETURN));

                method.instructions.insertBefore(lastInstruction, newInstructions);

                return method;
            }
        }
     }
  }