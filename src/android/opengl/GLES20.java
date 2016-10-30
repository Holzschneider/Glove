package android.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.opengl.GL11;

import de.dualuse.glove.Buffers;

public class GLES20 {
	
	public static final int GL_ES_VERSION_2_0 = 1;
    public static final int GL_DEPTH_BUFFER_BIT = 0x00000100;
    public static final int GL_STENCIL_BUFFER_BIT = 0x00000400;
    public static final int GL_COLOR_BUFFER_BIT = 0x00004000;
    public static final int GL_FALSE = 0;
    public static final int GL_TRUE = 1;
    public static final int GL_POINTS = 0x0000;
    public static final int GL_LINES = 0x0001;
    public static final int GL_LINE_LOOP = 0x0002;
    public static final int GL_LINE_STRIP = 0x0003;
    public static final int GL_TRIANGLES = 0x0004;
    public static final int GL_TRIANGLE_STRIP = 0x0005;
    public static final int GL_TRIANGLE_FAN = 0x0006;
    public static final int GL_ZERO = 0;
    public static final int GL_ONE = 1;
    public static final int GL_SRC_COLOR = 0x0300;
    public static final int GL_ONE_MINUS_SRC_COLOR = 0x0301;
    public static final int GL_SRC_ALPHA = 0x0302;
    public static final int GL_ONE_MINUS_SRC_ALPHA = 0x0303;
    public static final int GL_DST_ALPHA = 0x0304;
    public static final int GL_ONE_MINUS_DST_ALPHA = 0x0305;
    public static final int GL_DST_COLOR = 0x0306;
    public static final int GL_ONE_MINUS_DST_COLOR = 0x0307;
    public static final int GL_SRC_ALPHA_SATURATE = 0x0308;
    public static final int GL_FUNC_ADD = 0x8006;
    public static final int GL_BLEND_EQUATION = 0x8009;
    public static final int GL_BLEND_EQUATION_RGB = 0x8009;
    public static final int GL_BLEND_EQUATION_ALPHA = 0x883D;
    public static final int GL_FUNC_SUBTRACT = 0x800A;
    public static final int GL_FUNC_REVERSE_SUBTRACT = 0x800B;
    public static final int GL_BLEND_DST_RGB = 0x80C8;
    public static final int GL_BLEND_SRC_RGB = 0x80C9;
    public static final int GL_BLEND_DST_ALPHA = 0x80CA;
    public static final int GL_BLEND_SRC_ALPHA = 0x80CB;
    public static final int GL_CONSTANT_COLOR = 0x8001;
    public static final int GL_ONE_MINUS_CONSTANT_COLOR = 0x8002;
    public static final int GL_CONSTANT_ALPHA = 0x8003;
    public static final int GL_ONE_MINUS_CONSTANT_ALPHA = 0x8004;
    public static final int GL_BLEND_COLOR = 0x8005;
    public static final int GL_ARRAY_BUFFER = 0x8892;
    public static final int GL_ELEMENT_ARRAY_BUFFER = 0x8893;
    public static final int GL_ARRAY_BUFFER_BINDING = 0x8894;
    public static final int GL_ELEMENT_ARRAY_BUFFER_BINDING = 0x8895;
    public static final int GL_STREAM_DRAW = 0x88E0;
    public static final int GL_STATIC_DRAW = 0x88E4;
    public static final int GL_DYNAMIC_DRAW = 0x88E8;
    public static final int GL_BUFFER_SIZE = 0x8764;
    public static final int GL_BUFFER_USAGE = 0x8765;
    public static final int GL_CURRENT_VERTEX_ATTRIB = 0x8626;
    public static final int GL_FRONT = 0x0404;
    public static final int GL_BACK = 0x0405;
    public static final int GL_FRONT_AND_BACK = 0x0408;
    public static final int GL_TEXTURE_2D = 0x0DE1;
    public static final int GL_CULL_FACE = 0x0B44;
    public static final int GL_BLEND = 0x0BE2;
    public static final int GL_DITHER = 0x0BD0;
    public static final int GL_STENCIL_TEST = 0x0B90;
    public static final int GL_DEPTH_TEST = 0x0B71;
    public static final int GL_SCISSOR_TEST = 0x0C11;
    public static final int GL_POLYGON_OFFSET_FILL = 0x8037;
    public static final int GL_SAMPLE_ALPHA_TO_COVERAGE = 0x809E;
    public static final int GL_SAMPLE_COVERAGE = 0x80A0;
    public static final int GL_NO_ERROR = 0;
    public static final int GL_INVALID_ENUM = 0x0500;
    public static final int GL_INVALID_VALUE = 0x0501;
    public static final int GL_INVALID_OPERATION = 0x0502;
    public static final int GL_OUT_OF_MEMORY = 0x0505;
    public static final int GL_CW = 0x0900;
    public static final int GL_CCW = 0x0901;
    public static final int GL_LINE_WIDTH = 0x0B21;
    public static final int GL_ALIASED_POINT_SIZE_RANGE = 0x846D;
    public static final int GL_ALIASED_LINE_WIDTH_RANGE = 0x846E;
    public static final int GL_CULL_FACE_MODE = 0x0B45;
    public static final int GL_FRONT_FACE = 0x0B46;
    public static final int GL_DEPTH_RANGE = 0x0B70;
    public static final int GL_DEPTH_WRITEMASK = 0x0B72;
    public static final int GL_DEPTH_CLEAR_VALUE = 0x0B73;
    public static final int GL_DEPTH_FUNC = 0x0B74;
    public static final int GL_STENCIL_CLEAR_VALUE = 0x0B91;
    public static final int GL_STENCIL_FUNC = 0x0B92;
    public static final int GL_STENCIL_FAIL = 0x0B94;
    public static final int GL_STENCIL_PASS_DEPTH_FAIL = 0x0B95;
    public static final int GL_STENCIL_PASS_DEPTH_PASS = 0x0B96;
    public static final int GL_STENCIL_REF = 0x0B97;
    public static final int GL_STENCIL_VALUE_MASK = 0x0B93;
    public static final int GL_STENCIL_WRITEMASK = 0x0B98;
    public static final int GL_STENCIL_BACK_FUNC = 0x8800;
    public static final int GL_STENCIL_BACK_FAIL = 0x8801;
    public static final int GL_STENCIL_BACK_PASS_DEPTH_FAIL = 0x8802;
    public static final int GL_STENCIL_BACK_PASS_DEPTH_PASS = 0x8803;
    public static final int GL_STENCIL_BACK_REF = 0x8CA3;
    public static final int GL_STENCIL_BACK_VALUE_MASK = 0x8CA4;
    public static final int GL_STENCIL_BACK_WRITEMASK = 0x8CA5;
    public static final int GL_VIEWPORT = 0x0BA2;
    public static final int GL_SCISSOR_BOX = 0x0C10;
    public static final int GL_COLOR_CLEAR_VALUE = 0x0C22;
    public static final int GL_COLOR_WRITEMASK = 0x0C23;
    public static final int GL_UNPACK_ALIGNMENT = 0x0CF5;
    public static final int GL_PACK_ALIGNMENT = 0x0D05;
    public static final int GL_MAX_TEXTURE_SIZE = 0x0D33;
    public static final int GL_MAX_VIEWPORT_DIMS = 0x0D3A;
    public static final int GL_SUBPIXEL_BITS = 0x0D50;
    public static final int GL_RED_BITS = 0x0D52;
    public static final int GL_GREEN_BITS = 0x0D53;
    public static final int GL_BLUE_BITS = 0x0D54;
    public static final int GL_ALPHA_BITS = 0x0D55;
    public static final int GL_DEPTH_BITS = 0x0D56;
    public static final int GL_STENCIL_BITS = 0x0D57;
    public static final int GL_POLYGON_OFFSET_UNITS = 0x2A00;
    public static final int GL_POLYGON_OFFSET_FACTOR = 0x8038;
    public static final int GL_TEXTURE_BINDING_2D = 0x8069;
    public static final int GL_SAMPLE_BUFFERS = 0x80A8;
    public static final int GL_SAMPLES = 0x80A9;
    public static final int GL_SAMPLE_COVERAGE_VALUE = 0x80AA;
    public static final int GL_SAMPLE_COVERAGE_INVERT = 0x80AB;
    public static final int GL_NUM_COMPRESSED_TEXTURE_FORMATS = 0x86A2;
    public static final int GL_COMPRESSED_TEXTURE_FORMATS = 0x86A3;
    public static final int GL_DONT_CARE = 0x1100;
    public static final int GL_FASTEST = 0x1101;
    public static final int GL_NICEST = 0x1102;
    public static final int GL_GENERATE_MIPMAP_HINT = 0x8192;
    public static final int GL_BYTE = 0x1400;
    public static final int GL_UNSIGNED_BYTE = 0x1401;
    public static final int GL_SHORT = 0x1402;
    public static final int GL_UNSIGNED_SHORT = 0x1403;
    public static final int GL_INT = 0x1404;
    public static final int GL_UNSIGNED_INT = 0x1405;
    public static final int GL_FLOAT = 0x1406;
    public static final int GL_FIXED = 0x140C;
    public static final int GL_DEPTH_COMPONENT = 0x1902;
    public static final int GL_ALPHA = 0x1906;
    public static final int GL_RGB = 0x1907;
    public static final int GL_RGBA = 0x1908;
    public static final int GL_LUMINANCE = 0x1909;
    public static final int GL_LUMINANCE_ALPHA = 0x190A;
    public static final int GL_UNSIGNED_SHORT_4_4_4_4 = 0x8033;
    public static final int GL_UNSIGNED_SHORT_5_5_5_1 = 0x8034;
    public static final int GL_UNSIGNED_SHORT_5_6_5 = 0x8363;
    public static final int GL_FRAGMENT_SHADER = 0x8B30;
    public static final int GL_VERTEX_SHADER = 0x8B31;
    public static final int GL_MAX_VERTEX_ATTRIBS = 0x8869;
    public static final int GL_MAX_VERTEX_UNIFORM_VECTORS = 0x8DFB;
    public static final int GL_MAX_VARYING_VECTORS = 0x8DFC;
    public static final int GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS = 0x8B4D;
    public static final int GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS = 0x8B4C;
    public static final int GL_MAX_TEXTURE_IMAGE_UNITS = 0x8872;
    public static final int GL_MAX_FRAGMENT_UNIFORM_VECTORS = 0x8DFD;
    public static final int GL_SHADER_TYPE = 0x8B4F;
    public static final int GL_DELETE_STATUS = 0x8B80;
    public static final int GL_LINK_STATUS = 0x8B82;
    public static final int GL_VALIDATE_STATUS = 0x8B83;
    public static final int GL_ATTACHED_SHADERS = 0x8B85;
    public static final int GL_ACTIVE_UNIFORMS = 0x8B86;
    public static final int GL_ACTIVE_UNIFORM_MAX_LENGTH = 0x8B87;
    public static final int GL_ACTIVE_ATTRIBUTES = 0x8B89;
    public static final int GL_ACTIVE_ATTRIBUTE_MAX_LENGTH = 0x8B8A;
    public static final int GL_SHADING_LANGUAGE_VERSION = 0x8B8C;
    public static final int GL_CURRENT_PROGRAM = 0x8B8D;
    public static final int GL_NEVER = 0x0200;
    public static final int GL_LESS = 0x0201;
    public static final int GL_EQUAL = 0x0202;
    public static final int GL_LEQUAL = 0x0203;
    public static final int GL_GREATER = 0x0204;
    public static final int GL_NOTEQUAL = 0x0205;
    public static final int GL_GEQUAL = 0x0206;
    public static final int GL_ALWAYS = 0x0207;
    public static final int GL_KEEP = 0x1E00;
    public static final int GL_REPLACE = 0x1E01;
    public static final int GL_INCR = 0x1E02;
    public static final int GL_DECR = 0x1E03;
    public static final int GL_INVERT = 0x150A;
    public static final int GL_INCR_WRAP = 0x8507;
    public static final int GL_DECR_WRAP = 0x8508;
    public static final int GL_VENDOR = 0x1F00;
    public static final int GL_RENDERER = 0x1F01;
    public static final int GL_VERSION = 0x1F02;
    public static final int GL_EXTENSIONS = 0x1F03;
    public static final int GL_NEAREST = 0x2600;
    public static final int GL_LINEAR = 0x2601;
    public static final int GL_NEAREST_MIPMAP_NEAREST = 0x2700;
    public static final int GL_LINEAR_MIPMAP_NEAREST = 0x2701;
    public static final int GL_NEAREST_MIPMAP_LINEAR = 0x2702;
    public static final int GL_LINEAR_MIPMAP_LINEAR = 0x2703;
    public static final int GL_TEXTURE_MAG_FILTER = 0x2800;
    public static final int GL_TEXTURE_MIN_FILTER = 0x2801;
    public static final int GL_TEXTURE_WRAP_S = 0x2802;
    public static final int GL_TEXTURE_WRAP_T = 0x2803;
    public static final int GL_TEXTURE = 0x1702;
    public static final int GL_TEXTURE_CUBE_MAP = 0x8513;
    public static final int GL_TEXTURE_BINDING_CUBE_MAP = 0x8514;
    public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_X = 0x8515;
    public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_X = 0x8516;
    public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Y = 0x8517;
    public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y = 0x8518;
    public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Z = 0x8519;
    public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z = 0x851A;
    public static final int GL_MAX_CUBE_MAP_TEXTURE_SIZE = 0x851C;
    public static final int GL_TEXTURE0 = 0x84C0;
    public static final int GL_TEXTURE1 = 0x84C1;
    public static final int GL_TEXTURE2 = 0x84C2;
    public static final int GL_TEXTURE3 = 0x84C3;
    public static final int GL_TEXTURE4 = 0x84C4;
    public static final int GL_TEXTURE5 = 0x84C5;
    public static final int GL_TEXTURE6 = 0x84C6;
    public static final int GL_TEXTURE7 = 0x84C7;
    public static final int GL_TEXTURE8 = 0x84C8;
    public static final int GL_TEXTURE9 = 0x84C9;
    public static final int GL_TEXTURE10 = 0x84CA;
    public static final int GL_TEXTURE11 = 0x84CB;
    public static final int GL_TEXTURE12 = 0x84CC;
    public static final int GL_TEXTURE13 = 0x84CD;
    public static final int GL_TEXTURE14 = 0x84CE;
    public static final int GL_TEXTURE15 = 0x84CF;
    public static final int GL_TEXTURE16 = 0x84D0;
    public static final int GL_TEXTURE17 = 0x84D1;
    public static final int GL_TEXTURE18 = 0x84D2;
    public static final int GL_TEXTURE19 = 0x84D3;
    public static final int GL_TEXTURE20 = 0x84D4;
    public static final int GL_TEXTURE21 = 0x84D5;
    public static final int GL_TEXTURE22 = 0x84D6;
    public static final int GL_TEXTURE23 = 0x84D7;
    public static final int GL_TEXTURE24 = 0x84D8;
    public static final int GL_TEXTURE25 = 0x84D9;
    public static final int GL_TEXTURE26 = 0x84DA;
    public static final int GL_TEXTURE27 = 0x84DB;
    public static final int GL_TEXTURE28 = 0x84DC;
    public static final int GL_TEXTURE29 = 0x84DD;
    public static final int GL_TEXTURE30 = 0x84DE;
    public static final int GL_TEXTURE31 = 0x84DF;
    public static final int GL_ACTIVE_TEXTURE = 0x84E0;
    public static final int GL_REPEAT = 0x2901;
    public static final int GL_CLAMP_TO_EDGE = 0x812F;
    public static final int GL_MIRRORED_REPEAT = 0x8370;
    public static final int GL_FLOAT_VEC2 = 0x8B50;
    public static final int GL_FLOAT_VEC3 = 0x8B51;
    public static final int GL_FLOAT_VEC4 = 0x8B52;
    public static final int GL_INT_VEC2 = 0x8B53;
    public static final int GL_INT_VEC3 = 0x8B54;
    public static final int GL_INT_VEC4 = 0x8B55;
    public static final int GL_BOOL = 0x8B56;
    public static final int GL_BOOL_VEC2 = 0x8B57;
    public static final int GL_BOOL_VEC3 = 0x8B58;
    public static final int GL_BOOL_VEC4 = 0x8B59;
    public static final int GL_FLOAT_MAT2 = 0x8B5A;
    public static final int GL_FLOAT_MAT3 = 0x8B5B;
    public static final int GL_FLOAT_MAT4 = 0x8B5C;
    public static final int GL_SAMPLER_2D = 0x8B5E;
    public static final int GL_SAMPLER_CUBE = 0x8B60;
    public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED = 0x8622;
    public static final int GL_VERTEX_ATTRIB_ARRAY_SIZE = 0x8623;
    public static final int GL_VERTEX_ATTRIB_ARRAY_STRIDE = 0x8624;
    public static final int GL_VERTEX_ATTRIB_ARRAY_TYPE = 0x8625;
    public static final int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED = 0x886A;
    public static final int GL_VERTEX_ATTRIB_ARRAY_POINTER = 0x8645;
    public static final int GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 0x889F;
    public static final int GL_IMPLEMENTATION_COLOR_READ_TYPE = 0x8B9A;
    public static final int GL_IMPLEMENTATION_COLOR_READ_FORMAT = 0x8B9B;
    public static final int GL_COMPILE_STATUS = 0x8B81;
    public static final int GL_INFO_LOG_LENGTH = 0x8B84;
    public static final int GL_SHADER_SOURCE_LENGTH = 0x8B88;
    public static final int GL_SHADER_COMPILER = 0x8DFA;
    public static final int GL_SHADER_BINARY_FORMATS = 0x8DF8;
    public static final int GL_NUM_SHADER_BINARY_FORMATS = 0x8DF9;
    public static final int GL_LOW_FLOAT = 0x8DF0;
    public static final int GL_MEDIUM_FLOAT = 0x8DF1;
    public static final int GL_HIGH_FLOAT = 0x8DF2;
    public static final int GL_LOW_INT = 0x8DF3;
    public static final int GL_MEDIUM_INT = 0x8DF4;
    public static final int GL_HIGH_INT = 0x8DF5;
    public static final int GL_FRAMEBUFFER = 0x8D40;
    public static final int GL_RENDERBUFFER = 0x8D41;
    public static final int GL_RGBA4 = 0x8056;
    public static final int GL_RGB5_A1 = 0x8057;
    public static final int GL_RGB565 = 0x8D62;
    public static final int GL_DEPTH_COMPONENT16 = 0x81A5;
    public static final int GL_STENCIL_INDEX = 0x1901;
    public static final int GL_STENCIL_INDEX8 = 0x8D48;
    public static final int GL_RENDERBUFFER_WIDTH = 0x8D42;
    public static final int GL_RENDERBUFFER_HEIGHT = 0x8D43;
    public static final int GL_RENDERBUFFER_INTERNAL_FORMAT = 0x8D44;
    public static final int GL_RENDERBUFFER_RED_SIZE = 0x8D50;
    public static final int GL_RENDERBUFFER_GREEN_SIZE = 0x8D51;
    public static final int GL_RENDERBUFFER_BLUE_SIZE = 0x8D52;
    public static final int GL_RENDERBUFFER_ALPHA_SIZE = 0x8D53;
    public static final int GL_RENDERBUFFER_DEPTH_SIZE = 0x8D54;
    public static final int GL_RENDERBUFFER_STENCIL_SIZE = 0x8D55;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 0x8CD0;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 0x8CD1;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 0x8CD2;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 0x8CD3;
    public static final int GL_COLOR_ATTACHMENT0 = 0x8CE0;
    public static final int GL_DEPTH_ATTACHMENT = 0x8D00;
    public static final int GL_STENCIL_ATTACHMENT = 0x8D20;
    public static final int GL_NONE = 0;
    public static final int GL_FRAMEBUFFER_COMPLETE = 0x8CD5;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 0x8CD6;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 0x8CD7;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 0x8CD9;
    public static final int GL_FRAMEBUFFER_UNSUPPORTED = 0x8CDD;
    public static final int GL_FRAMEBUFFER_BINDING = 0x8CA6;
    public static final int GL_RENDERBUFFER_BINDING = 0x8CA7;
    public static final int GL_MAX_RENDERBUFFER_SIZE = 0x84E8;
    public static final int GL_INVALID_FRAMEBUFFER_OPERATION = 0x0506;

    public static void glActiveTexture ( int texture ) { throw new RuntimeException(); }

    public static void glAttachShader ( int program, int shader ) { 
    	org.lwjgl.opengl.GL20.glAttachShader( program, shader );
    }

    public static void glBindAttribLocation ( int program, int index, String name ) { throw new RuntimeException(); }

    public static void glBindBuffer ( int target, int buffer ) { 
    	org.lwjgl.opengl.GL15.glBindBuffer(target, buffer);
    }

    public static void glBindFramebuffer ( int target, int framebuffer ) { throw new RuntimeException(); }

    public static void glBindRenderbuffer ( int target, int renderbuffer ) { throw new RuntimeException(); }

    public static void glBindTexture ( int target, int texture ) {
    	org.lwjgl.opengl.GL11.glBindTexture(target, texture);
    }

    public static void glBlendColor ( float red, float green, float blue, float alpha ) { throw new RuntimeException(); }

    public static void glBlendEquation (  int mode  ) { throw new RuntimeException(); }

    public static void glBlendEquationSeparate ( int modeRGB, int modeAlpha ) { throw new RuntimeException(); }

    public static void glBlendFunc ( int sfactor, int dfactor ) { throw new RuntimeException(); }

    public static void glBlendFuncSeparate ( int srcRGB, int dstRGB, int srcAlpha, int dstAlpha ) { throw new RuntimeException(); }

    public static void glBufferData ( int target, int size, Buffer data, int usage ) {
    	if (data instanceof ByteBuffer) 
    		org.lwjgl.opengl.GL15.glBufferData(target, (ByteBuffer)data, usage);
    	else if (data instanceof FloatBuffer)
    		org.lwjgl.opengl.GL15.glBufferData(target, (FloatBuffer)data, usage);
    	else if (data instanceof IntBuffer)
    		org.lwjgl.opengl.GL15.glBufferData(target, (IntBuffer)data, usage);
    	else if (data instanceof ShortBuffer)
    		org.lwjgl.opengl.GL15.glBufferData(target, (ShortBuffer)data, usage);
    	else if (data instanceof DoubleBuffer)
    		org.lwjgl.opengl.GL15.glBufferData(target, (DoubleBuffer)data, usage);
    	else
    		throw new RuntimeException();
    }

    public static void glBufferSubData ( int target, int offset, int size, Buffer data ) {
    	if (data instanceof ByteBuffer) 
    		org.lwjgl.opengl.GL15.glBufferSubData(target, offset, (ByteBuffer)data);
    	else if (data instanceof FloatBuffer)
    		org.lwjgl.opengl.GL15.glBufferSubData(target, offset, (FloatBuffer)data);
    	else if (data instanceof IntBuffer)
    		org.lwjgl.opengl.GL15.glBufferSubData(target, offset, (IntBuffer)data);
    	else if (data instanceof ShortBuffer)
    		org.lwjgl.opengl.GL15.glBufferSubData(target, offset, (ShortBuffer)data);
    	else if (data instanceof DoubleBuffer)
    		org.lwjgl.opengl.GL15.glBufferSubData(target, offset, (DoubleBuffer)data);
    	else
    		throw new RuntimeException();
    }

    public static int glCheckFramebufferStatus ( int target ) { throw new RuntimeException(); }

    public static void glClear ( int mask ) { org.lwjgl.opengl.GL11.glClear(mask); }

    public static void glClearColor ( float red, float green, float blue, float alpha ) { org.lwjgl.opengl.GL11.glClearColor(red, green, blue, alpha); }

    public static void glClearDepthf ( float depth ) { throw new RuntimeException(); }

    public static void glClearStencil ( int s ) { throw new RuntimeException(); }

    public static void glColorMask ( boolean red, boolean green, boolean blue, boolean alpha ) { throw new RuntimeException(); }

    public static void glCompileShader ( int shader ) { 
    	org.lwjgl.opengl.GL20.glCompileShader(shader);
    }

    public static void glCompressedTexImage2D ( int target, int level, int internalformat, int width, int height, int border, int imageSize, Buffer data ) { throw new RuntimeException(); }

    public static void glCompressedTexSubImage2D ( int target, int level, int xoffset, int yoffset, int width, int height, int format, int imageSize, Buffer data ) { throw new RuntimeException(); }

    public static void glCopyTexImage2D ( int target, int level, int internalformat, int x, int y, int width, int height, int border ) { throw new RuntimeException(); }

    public static void glCopyTexSubImage2D ( int target, int level, int xoffset, int yoffset, int x, int y, int width, int height ) { throw new RuntimeException(); }

    public static int glCreateProgram (  ) { return org.lwjgl.opengl.GL20.glCreateProgram();  }

    public static int glCreateShader ( int type ) {  return org.lwjgl.opengl.GL20.glCreateShader(type); }

    public static void glCullFace ( int mode ) { org.lwjgl.opengl.GL11.glCullFace(mode); }

//    public static void glDeleteBuffers ( int n, IntBuffer buffers ) { throw new RuntimeException(); }

//    public static void glDeleteFramebuffers ( int n, IntBuffer framebuffers ) { throw new RuntimeException(); }

    public static void glDeleteProgram ( int program ) { throw new RuntimeException(); }

//    public static void glDeleteRenderbuffers ( int n, IntBuffer renderbuffers ) { throw new RuntimeException(); }

    public static void glDeleteShader ( int shader ) { throw new RuntimeException(); }

    public static void glDeleteTextures ( int n, IntBuffer textures ) {
    	textures.limit(n);
    	org.lwjgl.opengl.GL11.glDeleteTextures(textures);
    }

    public static void glDepthFunc ( int func ) { throw new RuntimeException(); }

    public static void glDepthMask ( boolean flag ) { throw new RuntimeException(); }

    public static void glDepthRangef ( float zNear, float zFar ) { throw new RuntimeException(); }

    public static void glDetachShader ( int program, int shader ) { throw new RuntimeException(); }

    public static void glDisable ( int cap ) { org.lwjgl.opengl.GL11.glDisable(cap); }

    public static void glDisableVertexAttribArray ( int index ) { org.lwjgl.opengl.GL20.glDisableVertexAttribArray(index); }

    public static void glDrawArrays ( int mode, int first, int count ) { org.lwjgl.opengl.GL11.glDrawArrays(mode, first, count); }

    public static void glDrawElements ( int mode, int count, int type, Buffer indices ) { throw new RuntimeException(); }
    
    public static void glDrawElements ( int mode, int count, int type, int indices ) { throw new RuntimeException(); }

    public static void glEnable ( int cap ) { org.lwjgl.opengl.GL11.glEnable(cap); }

    public static void glEnableVertexAttribArray ( int index ) { org.lwjgl.opengl.GL20.glEnableVertexAttribArray(index); }

    public static void glFinish (  ) { throw new RuntimeException(); }

    public static void glFlush (  ) { throw new RuntimeException(); }

    public static void glFramebufferRenderbuffer ( int target, int attachment, int renderbuffertarget, int renderbuffer ) { throw new RuntimeException(); }

    public static void glFramebufferTexture2D ( int target, int attachment, int textarget, int texture, int level ) { throw new RuntimeException(); }

    public static void glFrontFace ( int mode ) { throw new RuntimeException(); }

    public static void glGenBuffers ( int n, int[] buffers, int offset) {
    	IntBuffer directBuffers = ByteBuffer.allocateDirect(n*4).order(ByteOrder.nativeOrder()).asIntBuffer();
    	org.lwjgl.opengl.GL15.glGenBuffers(directBuffers);
    	directBuffers.put(buffers, offset, n);
    }

    public static void glGenerateMipmap ( int target ) { org.lwjgl.opengl.ARBFramebufferObject.glGenerateMipmap(target); }

    public static void glGenFramebuffers ( int n, IntBuffer framebuffers ) { throw new RuntimeException(); }

    public static void glGenRenderbuffers ( int n, IntBuffer renderbuffers ) { throw new RuntimeException(); }

    public static void glGenTextures ( int n, int[] textures, int offset) {
    	if (n==1) {
	    	IntBuffer name = Buffers.name();
	    	org.lwjgl.opengl.GL11.glGenTextures(name);
	    	name.put(textures, offset, n);
    	} else {
    		
    	}
    		
    }

    // deviates
    public static String glGetActiveAttrib ( int program, int index, IntBuffer size, Buffer type ) { return ""; }

    // deviates
    public static String glGetActiveUniform ( int program, int index, IntBuffer size, Buffer type ) { return ""; }

    public static void glGetAttachedShaders ( int program, int maxcount, Buffer count, IntBuffer shaders ) { throw new RuntimeException(); }

    public static int glGetAttribLocation ( int program, String name ) { return org.lwjgl.opengl.GL20.glGetAttribLocation(program, name); }

    public static void glGetBooleanv ( int pname, Buffer params ) { throw new RuntimeException(); }

    public static void glGetBufferParameteriv ( int target, int pname, IntBuffer params ) { throw new RuntimeException(); }

    public static int glGetError () { return org.lwjgl.opengl.GL11.glGetError(); }

    public static void glGetFloatv ( int pname, FloatBuffer params ) { throw new RuntimeException(); }

    public static void glGetFramebufferAttachmentParameteriv ( int target, int attachment, int pname, IntBuffer params ) { throw new RuntimeException(); }

    public static void glGetIntegerv ( int pname, IntBuffer params ) { throw new RuntimeException(); }

    public static void glGetProgramiv ( int program, int pname, IntBuffer params ) { throw new RuntimeException(); }

    // deviates
    public static String glGetProgramInfoLog ( int program ) { return ""; }

//    public static void glGetRenderbufferParameteriv ( int target, int pname, IntBuffer params ) { throw new RuntimeException(); }

    public static void glGetShaderiv ( int shader, int pname, int params[], int offset ) {
    	IntBuffer paramBuffer = ByteBuffer.allocateDirect((params.length-offset)*4).order(ByteOrder.nativeOrder()).asIntBuffer();
    	org.lwjgl.opengl.GL20.glGetShaderiv(shader, pname, paramBuffer);
    	paramBuffer.clear();
    	paramBuffer.put(params, offset, paramBuffer.capacity());
    }

    // deviates
    public static String glGetShaderInfoLog ( int shader ) {
    	return org.lwjgl.opengl.GL20.glGetShaderInfoLog(shader, 1024);
    }

//    public static void glGetShaderPrecisionFormat ( int shadertype, int precisiontype, IntBuffer range, IntBuffer precision ) { throw new RuntimeException(); }

    public static void glGetShaderSource ( int shader, int bufsize, Buffer length, String source ) { throw new RuntimeException(); }

    public static String glGetString ( int name ) { return "";  }

//    public static void glGetTexParameterfv ( int target, int pname, FloatBuffer params ) { throw new RuntimeException(); }
//
//    public static void glGetTexParameteriv ( int target, int pname, IntBuffer params ) { throw new RuntimeException(); }
//
//    public static void glGetUniformfv ( int program, int location, FloatBuffer params ) { throw new RuntimeException(); }
//
//    public static void glGetUniformiv ( int program, int location, IntBuffer params ) { throw new RuntimeException(); }

    public static int glGetUniformLocation ( int program, String name ) { return org.lwjgl.opengl.GL20.glGetUniformLocation(program, name); }

//    public static void glGetVertexAttribfv ( int index, int pname, FloatBuffer params ) { throw new RuntimeException(); }
//
//    public static void glGetVertexAttribiv ( int index, int pname, IntBuffer params ) { throw new RuntimeException(); }

    public static void glGetVertexAttribPointerv ( int index, int pname, Buffer pointer ) { throw new RuntimeException(); }

    public static void glHint ( int target, int mode ) { throw new RuntimeException(); }

//    public boolean glIsBuffer ( int buffer ) { throw new RuntimeException(); }
//
//    public boolean glIsEnabled ( int cap ) { throw new RuntimeException(); }
//
//    public boolean glIsFramebuffer ( int framebuffer ) { throw new RuntimeException(); }
//
//    public boolean glIsProgram ( int program ) { throw new RuntimeException(); }
//
//    public boolean glIsRenderbuffer ( int renderbuffer ) { throw new RuntimeException(); }
//
//    public boolean glIsShader ( int shader ) { throw new RuntimeException(); }
//
//    public boolean glIsTexture ( int texture ) { throw new RuntimeException(); }

    public static void glLineWidth ( float width ) { throw new RuntimeException(); }

    public static void glLinkProgram ( int program ) {  org.lwjgl.opengl.GL20.glLinkProgram(program); }

    public static void glPixelStorei ( int pname, int param ) { throw new RuntimeException(); }

    public static void glPolygonOffset ( float factor, float units ) { throw new RuntimeException(); }

    public static void glReadPixels ( int x, int y, int width, int height, int format, int type, Buffer pixels ) { throw new RuntimeException(); }

    public static void glReleaseShaderCompiler (  ) { throw new RuntimeException(); }

    public static void glRenderbufferStorage ( int target, int internalformat, int width, int height ) { throw new RuntimeException(); }

    public static void glSampleCoverage ( float value, boolean invert ) { throw new RuntimeException(); }

    public static void glScissor ( int x, int y, int width, int height ) { throw new RuntimeException(); }

//    public static void glShaderBinary ( int n, IntBuffer shaders, int binaryformat, Buffer binary, int length ) { throw new RuntimeException(); }

    // Deviates
    public static void glShaderSource ( int shader, String string ) { 
    	org.lwjgl.opengl.GL20.glShaderSource(shader, string);
    }

    public static void glStencilFunc ( int func, int ref, int mask ) { throw new RuntimeException(); }

    public static void glStencilFuncSeparate ( int face, int func, int ref, int mask ) { throw new RuntimeException(); }

    public static void glStencilMask ( int mask ) { throw new RuntimeException(); }

    public static void glStencilMaskSeparate ( int face, int mask ) { throw new RuntimeException(); }

    public static void glStencilOp ( int fail, int zfail, int zpass ) { throw new RuntimeException(); }

    public static void glStencilOpSeparate ( int face, int fail, int zfail, int zpass ) { throw new RuntimeException(); }

    public static void glTexImage2D ( int target, int level, int internalformat, int width, int height, int border, int format, int type, Buffer pixels ) {
    	if (pixels == null)
    		org.lwjgl.opengl.GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, 0L);
    	else
    	if (pixels instanceof ByteBuffer)
    		org.lwjgl.opengl.GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, (ByteBuffer) pixels);
    	else if (pixels instanceof IntBuffer)
    		org.lwjgl.opengl.GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, (IntBuffer) pixels);
    	else if (pixels instanceof FloatBuffer)
    		org.lwjgl.opengl.GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, (FloatBuffer) pixels);
    	else if (pixels instanceof ShortBuffer)
    		org.lwjgl.opengl.GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, (ShortBuffer) pixels);
    	else
    		throw new RuntimeException();
    }

    public static void glTexParameterf ( int target, int pname, float param ) {  org.lwjgl.opengl.GL11.glTexParameterf(target, pname, param);  }

//    public static void glTexParameterfv ( int target, int pname, FloatBuffer params ) { throw new RuntimeException(); }

    public static void glTexParameteri ( int target, int pname, int param ) { org.lwjgl.opengl.GL11.glTexParameteri(target, pname, param); }

//    public static void glTexParameteriv ( int target, int pname, IntBuffer params ) { throw new RuntimeException(); }

    public static void glTexSubImage2D ( int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, Buffer pixels ) {
    	if (pixels instanceof ByteBuffer)
    	   	org.lwjgl.opengl.GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, (ByteBuffer) pixels);
    	else if (pixels instanceof IntBuffer)
    	   	org.lwjgl.opengl.GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, (IntBuffer) pixels);
		else if (pixels instanceof FloatBuffer)
    	   	org.lwjgl.opengl.GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, (FloatBuffer) pixels);
		else if (pixels instanceof ShortBuffer)
    	   	org.lwjgl.opengl.GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, (ShortBuffer) pixels);
		else
			throw new RuntimeException();
    }

    public static void glUniform1f ( int location, float x ) { org.lwjgl.opengl.GL20.glUniform1f(location, x); }

//    public static void glUniform1fv ( int location, int count, FloatBuffer v ) { throw new RuntimeException(); }

    public static void glUniform1i ( int location, int x ) { org.lwjgl.opengl.GL20.glUniform1i(location, x); }

//    public static void glUniform1iv ( int location, int count, IntBuffer v ) { throw new RuntimeException(); }

    public static void glUniform2f ( int location, float x, float y ) { org.lwjgl.opengl.GL20.glUniform2f(location, x, y); }

//    public static void glUniform2fv ( int location, int count, FloatBuffer v ) { throw new RuntimeException(); }

    public static void glUniform2i ( int location, int x, int y ) { org.lwjgl.opengl.GL20.glUniform2i(location, x, y); }

//    public static void glUniform2iv ( int location, int count, IntBuffer v ) { throw new RuntimeException(); }

    public static void glUniform3f ( int location, float x, float y, float z ) { org.lwjgl.opengl.GL20.glUniform3f(location, x, y, z); }

//    public static void glUniform3fv ( int location, int count, FloatBuffer v ) { throw new RuntimeException(); }

    public static void glUniform3i ( int location, int x, int y, int z ) { org.lwjgl.opengl.GL20.glUniform3i(location, x, y, z); }

//    public static void glUniform3iv ( int location, int count, IntBuffer v ) { throw new RuntimeException(); }

    public static void glUniform4f ( int location, float x, float y, float z, float w ) { org.lwjgl.opengl.GL20.glUniform4f(location, x, y, z, w); }

//    public static void glUniform4fv ( int location, int count, float[] v, int offset ) { throw new RuntimeException(); }

    public static void glUniform4i ( int location, int x, int y, int z, int w ) { org.lwjgl.opengl.GL20.glUniform4i(location, x, y, z,w); }

//    public static void glUniform4iv ( int location, int count, int[] v, int offset ) { throw new RuntimeException(); }

    public static void glUniformMatrix2fv ( int location, int count, boolean transpose, float[] value, int offset) { throw new RuntimeException(); }

    public static void glUniformMatrix3fv ( int location, int count, boolean transpose, float[] value, int offset) { throw new RuntimeException(); }

    public static void glUniformMatrix4fv ( int location, int count, boolean transpose, float[] value, int offset) {
    	org.lwjgl.opengl.GL20.glUniformMatrix4fv(location, transpose, Buffers.matrix4f(value));
    }

    public static void glUseProgram ( int program ) { org.lwjgl.opengl.GL20.glUseProgram(program); }

    public static void glValidateProgram ( int program ) { throw new RuntimeException(); }

    public static void glVertexAttrib1f ( int indx, float x ) { throw new RuntimeException(); }

//    public static void glVertexAttrib1fv ( int indx, FloatBuffer values ) { throw new RuntimeException(); }

    public static void glVertexAttrib2f ( int indx, float x, float y ) { throw new RuntimeException(); }

//    public static void glVertexAttrib2fv ( int indx, FloatBuffer values ) { throw new RuntimeException(); }

    public static void glVertexAttrib3f ( int indx, float x, float y, float z ) { throw new RuntimeException(); }

//    public static void glVertexAttrib3fv ( int indx, FloatBuffer values ) { throw new RuntimeException(); }

    public static void glVertexAttrib4f ( int indx, float x, float y, float z, float w ) { throw new RuntimeException(); }

//    public static void glVertexAttrib4fv ( int indx, FloatBuffer values ) { throw new RuntimeException(); }

    public static void glVertexAttribPointer ( int indx, int size, int type, boolean normalized, int stride, Buffer ptr ) {
    	
    	
    	if (ptr instanceof ByteBuffer) {
        	switch (type) {
        	case org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE: org.lwjgl.opengl.GL20.glVertexAttribPointer(indx, size, type, normalized, stride, (ByteBuffer)ptr); break;
        	case org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT: org.lwjgl.opengl.GL20.glVertexAttribPointer(indx, size, type, normalized, stride, (ByteBuffer)ptr); break;
        	case org.lwjgl.opengl.GL11.GL_UNSIGNED_INT: org.lwjgl.opengl.GL20.glVertexAttribPointer(indx, size, type, normalized, stride, (ByteBuffer)ptr); break;
        	case org.lwjgl.opengl.GL11.GL_BYTE: org.lwjgl.opengl.GL20.glVertexAttribPointer(indx, size, type, normalized, stride, (ByteBuffer)ptr); break;
        	case org.lwjgl.opengl.GL11.GL_SHORT: org.lwjgl.opengl.GL20.glVertexAttribPointer(indx, size, type, normalized, stride, (ByteBuffer)ptr); break;
        	case org.lwjgl.opengl.GL11.GL_INT: org.lwjgl.opengl.GL20.glVertexAttribPointer(indx, size, type, normalized, stride, (ByteBuffer)ptr ); break;
        	case org.lwjgl.opengl.GL11.GL_FLOAT: org.lwjgl.opengl.GL20.glVertexAttribPointer(indx, size, type, normalized, stride, (ByteBuffer)ptr ); break;
        	case org.lwjgl.opengl.GL11.GL_DOUBLE: org.lwjgl.opengl.GL20.glVertexAttribPointer(indx, size, type, normalized, stride, (ByteBuffer)ptr ); break;
        	default: throw new RuntimeException();
        	};
//    		org.lwjgl.opengl.GL20.glVertexAttribPointer(indx, size, true, normalized, stride, (ByteBuffer)ptr); else
    	} else 
//    		public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, FloatBuffer pointer) {
		if (ptr instanceof FloatBuffer) org.lwjgl.opengl.GL20.glVertexAttribPointer(indx, size, GL_FLOAT, normalized, stride, (FloatBuffer)ptr); else
		if (ptr instanceof IntBuffer) org.lwjgl.opengl.GL20.glVertexAttribPointer(indx, size, GL_INT, normalized, stride, (IntBuffer)ptr); else
    	if (ptr instanceof ShortBuffer) org.lwjgl.opengl.GL20.glVertexAttribPointer(indx, size, GL_SHORT, normalized, stride, (IntBuffer)ptr); else
    		throw new RuntimeException();
//    		
	}
    
    public static void glVertexAttribPointer ( int indx, int size, int type, boolean normalized, int stride, int ptr ) {
    	org.lwjgl.opengl.GL20.glVertexAttribPointer(indx, size, type, normalized, stride, ptr);
    }

    public static void glViewport ( int x, int y, int width, int height ) { org.lwjgl.opengl.GL11.glViewport(x, y, width, height); }
    
}
