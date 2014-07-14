  /** @jsx React.DOM */

  var SearchBox = React.createClass({displayName: 'SearchBox',
    getInitialState: function() {
      this.props.scope.query="Zed's dead...";
      return {
        prompt: "Zed's dead...",
        query: "Zed's dead..."
      };
    },  

    handleChange: function(){
      var _this=this;
      this.state.query=this.refs.quoteTextInput.getDOMNode().value; 
      var scope=this.props.scope
      scope.search(this.state.query);
    }, 

    render: function() {     
        return (
            React.DOM.form( {onSubmit:this.handleChange}, 
              React.DOM.div( {className:"searchContainer"} , 
                React.DOM.input( 
                  {type:"text", 
                  'ng-model':"search.query",
                  className:"searchBar", 
                  placeholder:this.state.prompt, 
                  ref:"quoteTextInput"} 
                  //value={this.state.query} 
                  ),
                React.DOM.span( {className:"glyphicon glyphicon-search searchButton"}  )
              )  
            )
        );
    }
  });


var Scene=React.createClass({displayName: 'Scene',
  render: function(){
    return (
          React.DOM.div( {className:"col-sm-6 col-md-6"}, 
            React.DOM.div( {className:"thumbnail", onClick:this.handleClick},       
              React.DOM.img( {src:this.props.scene.urls[0], alt:this.props.scene.caption}),                          
              React.DOM.blockquote(null, 
                React.DOM.p(null, this.props.scene.caption)
              )
            )
          )                   
    )    
  },

  handleClick: function(event) {
    
  },

});  

/** @jsx React.DOM */
var Clip = React.createClass({displayName: 'Clip',
    render: function() {
      ///var scope = this.props.scope;
      var images = [];

      this.props.scope.data.forEach(function(scene){
        images.push(
              Scene( {scene:scene, key:scene.id})
            )
      }.bind(this));
      return (
          React.DOM.div( {className:"thumbnails"}, 
            images
          )
      )      
    }
});