export interface UiMetadata{
  tag:string[];
  cssClassNames?:string[];
  containerId?:number[];
  order?:number[];
}
class Element{
  children?: Element[];
  readonly content?: string;
  readonly order: number;
  readonly tag: string;
  readonly classes : string;
  toString():string{
    return `<${this.tag}${this.classes.length>0?" class="+this.classes:""}>`+ (this.content?this.content :this.children?.sort((c1,c2)=> c1.order-c2.order).join("\n")) + `</${this.tag}>`;
  }
  constructor(tag:string="div", classes:string="", order:number=0,inner?:string ) {
    this.tag = tag;
    this.order = order;
    this.classes = classes;
    this.content = inner;
  }
}
export default class TypelessHTMLFormatter {
  private metadatas : Array<[string,UiMetadata]>;
  public constructor( metadata:Array<[string, UiMetadata]>) {
    this.metadatas = metadata;
  }
  public format(rootName : string,root : any) : string{
    const containers : Map<string, [Element,number]>= new Map();
    return Object.keys(root).map(field =>({metadata: (this.metadatas.find(e=>(e[0].match(`^${rootName}-${field}(?::.*)?$`)))??["",<UiMetadata>{tag:["div"]}])[1], field}))
      .map((fieldWithMetadata) =>{
        return getHTML(root[fieldWithMetadata.field],0, fieldWithMetadata.metadata);
      }).sort((c1,c2)=>c1.order-c2.order).filter((value, index, array) => array.indexOf(value)===index).map(e=>e.toString()).join("\n");
    function getHTML(inner : string |Element,level:number,metadata?:UiMetadata): Element {
      if (!metadata) return new Element("div", "", 0, <string>inner);
      else if (inner instanceof Element && metadata.tag.length==0) return inner;
      const tag = metadata.tag.shift()??"div";
      const cssClasses = metadata.cssClassNames?.shift()??"";
      const order = metadata.order?.shift()??0;
      if (typeof inner === "string") return getHTML( new Element(tag,cssClasses,order, inner),level+1,metadata);
      let containerWithLevel : [Element,number]|undefined;
      let container:Element;
      if (!metadata?.containerId || !(containerWithLevel=containers.get(metadata.containerId.toString())) || containerWithLevel[1] < level) {
        container = new Element(tag, cssClasses, order);
        if (metadata.containerId) containers.set(metadata.containerId.toString(), [container,0]);
      } else container = containerWithLevel[0];
      if (!container.children) container.children = [];
      container.children.push(inner);
      return getHTML(container, level+1,metadata);
    }
  }
}
